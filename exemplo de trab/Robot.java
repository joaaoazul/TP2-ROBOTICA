import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Represents an autonomous robot in the installation.
 */
public class Robot {
    private static final double MOVE_COST = 0.5;
    private static final double IDLE_OUTSIDE_STATION_COST = 0.2;
    private static final double CHARGE_RATE = 10.0;

    private final int id;
    private Position position;
    private final Position chargingStation;

    private double charge;
    private RobotState state;
    private BusyPhase busyPhase;

    private Task currentTask;
    private FactoryObject carriedObject;

    private List<Position> activePath;
    private int activePathIndex;
    private int segmentProgress;
    private int segmentLength;

    private List<Position> planToObject;
    private List<Position> planToDestination;
    private List<Position> planToStation;

    /**
     * Creates a robot.
     *
     * @param id robot id
     * @param startPosition initial robot position
     * @param chargingStation robot charging station
     */
    public Robot(int id, Position startPosition, Position chargingStation) {
        this.id = id;
        this.position = startPosition;
        this.chargingStation = chargingStation;
        this.charge = 100.0;
        this.state = RobotState.IDLE;
        this.busyPhase = BusyPhase.NONE;
        this.currentTask = null;
        this.carriedObject = null;
        this.activePath = new ArrayList<>();
        this.activePathIndex = 0;
        this.segmentProgress = 0;
        this.segmentLength = 0;
        this.planToObject = new ArrayList<>();
        this.planToDestination = new ArrayList<>();
        this.planToStation = new ArrayList<>();
    }

    /**
     * Gets robot id.
     *
     * @return robot id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets robot position.
     *
     * @return current position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gets robot charging station.
     *
     * @return charging station position
     */
    public Position getChargingStation() {
        return chargingStation;
    }

    /**
     * Gets current battery charge.
     *
     * @return battery charge percentage
     */
    public double getCharge() {
        return charge;
    }

    /**
     * Gets state.
     *
     * @return robot state
     */
    public RobotState getState() {
        return state;
    }

    /**
     * Gets current task.
     *
     * @return task or null
     */
    public Task getCurrentTask() {
        return currentTask;
    }

    /**
     * Indicates if robot is carrying an object.
     *
     * @return true if carrying
     */
    public boolean isCarryingObject() {
        return carriedObject != null;
    }

    /**
     * Updates robot behavior for one simulation iteration.
     *
     * @param simulation current simulation
     */
    public void tick(Simulation simulation) {
        if (state == RobotState.CHARGING) {
            charge = Math.min(100.0, charge + CHARGE_RATE);
            if (charge >= 100.0) {
                charge = 100.0;
                state = RobotState.IDLE;
                busyPhase = BusyPhase.NONE;
            }
            return;
        }

        if (state == RobotState.BUSY) {
            executeBusyTick(simulation);
            return;
        }

        if (position.equals(chargingStation) && charge < 100.0) {
            state = RobotState.CHARGING;
            busyPhase = BusyPhase.NONE;
            charge = Math.min(100.0, charge + CHARGE_RATE);
            if (charge >= 100.0) {
                charge = 100.0;
                state = RobotState.IDLE;
            }
            return;
        }

        tryAssignTask(simulation);
        if (state == RobotState.BUSY) {
            return;
        }

        if (!position.equals(chargingStation)) {
            charge = Math.max(0.0, charge - IDLE_OUTSIDE_STATION_COST);
        }
    }

    /**
     * Returns robot status line for output.
     *
     * @return status line
     */
    public String toStatusLine() {
        StringBuilder builder = new StringBuilder();
        builder.append("[R").append(id).append("] ");
        builder.append("pos=").append(position.toCoordinateString()).append(" ");
        builder.append(String.format(Locale.US, "charge=%.2f%% ", charge));
        builder.append("state=").append(getStateText()).append(" ");
        builder.append("task=").append(getTaskText());
        return builder.toString();
    }

    /**
     * Returns whether robot is charging on its own station.
     *
     * @return true if charging on station
     */
    public boolean isChargingOnStation() {
        return state == RobotState.CHARGING && position.equals(chargingStation);
    }

    private String getTaskText() {
        if (currentTask == null) {
            return "NONE";
        }
        return currentTask.toTaskLabel();
    }

    private String getStateText() {
        if (state == RobotState.IDLE) {
            return "IDLE";
        }
        if (state == RobotState.CHARGING) {
            return "CHARGING";
        }

        String phase;
        if (busyPhase == BusyPhase.GOING_TO_OBJECT) {
            phase = "BUSY GOING TO OBJECT";
        } else if (busyPhase == BusyPhase.GOING_TO_DESTINATION) {
            phase = "BUSY GOING TO DESTINATION";
        } else {
            phase = "BUSY GOING TO CHARGING STATION";
        }

        int total = Math.max(1, segmentLength);
        int current = Math.min(total, Math.max(1, segmentProgress + 1));
        return phase + " (" + current + "/" + total + ")";
    }

    private void executeBusyTick(Simulation simulation) {
        if (activePathIndex < activePath.size()) {
            Position nextPosition = activePath.get(activePathIndex);
            position = nextPosition;
            activePathIndex++;
            segmentProgress++;
            charge = Math.max(0.0, charge - MOVE_COST);
            return;
        }

        if (busyPhase == BusyPhase.GOING_TO_OBJECT) {
            arriveAtObject(simulation);
            return;
        }

        if (busyPhase == BusyPhase.GOING_TO_DESTINATION) {
            arriveAtDestination(simulation);
            return;
        }

        if (busyPhase == BusyPhase.GOING_TO_CHARGING_STATION) {
            if (position.equals(chargingStation)) {
                state = RobotState.CHARGING;
                busyPhase = BusyPhase.NONE;
                activePath = new ArrayList<>();
                activePathIndex = 0;
                segmentProgress = 0;
                segmentLength = 0;
                charge = Math.min(100.0, charge + CHARGE_RATE);
                if (charge >= 100.0) {
                    charge = 100.0;
                    state = RobotState.IDLE;
                }
            }
        }
    }

    private void arriveAtObject(Simulation simulation) {
        if (currentTask == null) {
            goToChargingStation(simulation);
            return;
        }

        FactoryObject object = simulation.getObjectById(currentTask.getObjectId());
        if (object == null) {
            cancelCurrentTask();
            goToChargingStation(simulation);
            return;
        }

        carriedObject = object;
        carriedObject.setCarried(true);

        busyPhase = BusyPhase.GOING_TO_DESTINATION;
        setActivePath(planToDestination);
    }

    private void arriveAtDestination(Simulation simulation) {
        if (carriedObject != null) {
            carriedObject.setPosition(position);
            carriedObject.setCarried(false);
            carriedObject = null;
        }

        if (currentTask != null) {
            currentTask.setStatus(TaskStatus.COMPLETED);
            currentTask = null;
        }

        busyPhase = BusyPhase.NONE;
        activePath = new ArrayList<>();
        activePathIndex = 0;
        segmentProgress = 0;
        segmentLength = 0;
        state = RobotState.IDLE;

        tryAssignTask(simulation);
        if (state == RobotState.IDLE) {
            goToChargingStation(simulation);
        }
    }

    private void cancelCurrentTask() {
        if (currentTask != null) {
            currentTask.setStatus(TaskStatus.PENDING);
            currentTask.setAssignedRobotId(null);
            currentTask = null;
        }
        carriedObject = null;
    }

    private void tryAssignTask(Simulation simulation) {
        if (state != RobotState.IDLE) {
            return;
        }

        TaskAssignment assignment = simulation.findAssignmentForRobot(this);
        if (assignment == null) {
            return;
        }

        currentTask = assignment.task();
        currentTask.setStatus(TaskStatus.ASSIGNED);
        currentTask.setAssignedRobotId(id);

        planToObject = assignment.plan().getPathToObject();
        planToDestination = assignment.plan().getPathToDestination();
        planToStation = assignment.plan().getPathToStation();

        state = RobotState.BUSY;
        busyPhase = BusyPhase.GOING_TO_OBJECT;
        setActivePath(planToObject);
    }

    private void goToChargingStation(Simulation simulation) {
        if (position.equals(chargingStation)) {
            state = RobotState.CHARGING;
            busyPhase = BusyPhase.NONE;
            activePath = new ArrayList<>();
            activePathIndex = 0;
            segmentProgress = 0;
            segmentLength = 0;
            return;
        }

        List<Position> path = planToStation;
        if (path == null || path.isEmpty()) {
            path = simulation.findPath(position, chargingStation, null);
        }

        if (path == null) {
            state = RobotState.IDLE;
            busyPhase = BusyPhase.NONE;
            return;
        }

        state = RobotState.BUSY;
        busyPhase = BusyPhase.GOING_TO_CHARGING_STATION;
        setActivePath(path);
    }

    private void setActivePath(List<Position> path) {
        if (path == null) {
            path = new ArrayList<>();
        }
        activePath = new ArrayList<>(path);
        activePathIndex = 0;
        segmentProgress = 0;
        segmentLength = Math.max(1, activePath.size());
    }
}
