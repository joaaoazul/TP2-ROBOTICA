
// representar o comportamento autonomo do robo

import java.util.List;

public class Robot extends Entity {

    private int id;

    private double charge = 100.0;

    private RobotState state = RobotState.IDLE;

    private RobotPhase phase = RobotPhase.NONE;

    private ChargingStation chargingStation;

    private InitObject carriedObject = null;

    private Task currentTask = null;

    private List<Position> currentPath;

    private int currentPathIndex;


    public Robot(Position position, int id, ChargingStation chargingStation) {
        super(position);
        this.id = id;
        this.chargingStation = chargingStation;
    }


    public int getId() {return this.id; }

    public double getCharge() {return this.charge;}

    public RobotPhase getPhase() { return this.phase; }

    public RobotState getState() {return this.state;}

    public ChargingStation getChargingStation() {return this.chargingStation;}

    public
}

//ficam a faltar os getters e setters

// TODO: getters - getId, getCharge, getState, getPhase, getChargingStation, getCarriedObject, getCurrentTask, getCurrentPath, getCurrentPathIndex
// TODO: setters - setCharge, setState, setPhase, setCurrentTask, setCarriedObject, setCurrentPath, setCurrentPathIndex
// TODO: lógica - executeStep(Project project), tryPickUpTask(Project project), moveAlongPath(), pickUpObject(), dropObject(), canCompleteTask(Task task, Project project)


