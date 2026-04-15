import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Holds simulation state and provides operations for commands and robot updates.
 */
public class Simulation {
    private final int width;
    private final int height;
    private final Set<Position> obstacles;
    private final List<Robot> robots;
    private final Map<Integer, FactoryObject> objects;
    private final List<Task> tasks;
    private final PathFinder pathFinder;
    private int stepCount;

    /**
     * Creates simulation instance.
     *
     * @param width map width
     * @param height map height
     * @param obstacles obstacle positions
     * @param robots robot list
     * @param objects object map by id
     */
    public Simulation(int width, int height, Set<Position> obstacles, List<Robot> robots, Map<Integer, FactoryObject> objects) {
        this.width = width;
        this.height = height;
        this.obstacles = new HashSet<>(obstacles);
        this.robots = new ArrayList<>(robots);
        this.objects = new HashMap<>(objects);
        this.tasks = new ArrayList<>();
        this.pathFinder = new PathFinder();
        this.stepCount = 0;
    }

    /**
     * Returns true if position is inside map bounds.
     *
     * @param position position
     * @return true if inside
     */
    public boolean isInside(Position position) {
        return position.getX() >= 1
            && position.getX() <= width
            && position.getY() >= 1
            && position.getY() <= height;
    }

    /**
     * Returns whether a position is traversable in DFS according to assignment rules.
     *
     * @param position candidate position
     * @param goal goal position
     * @param allowedObjectId allowed object id for goal cell
     * @return true if accessible
     */
    public boolean isAccessibleForPath(Position position, Position goal, Integer allowedObjectId) {
        if (obstacles.contains(position)) {
            return false;
        }

        if (position.equals(goal)) {
            return true;
        }

        for (Robot robot : robots) {
            if (robot.getChargingStation().equals(position)) {
                return false;
            }
        }

        for (FactoryObject object : objects.values()) {
            if (!object.isCarried() && object.getPosition().equals(position)) {
                boolean isAllowedGoalObject = allowedObjectId != null
                    && object.getId() == allowedObjectId
                    && position.equals(goal);
                if (!isAllowedGoalObject) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Finds a path using DFS rules.
     *
     * @param start start position
     * @param goal goal position
     * @param allowedObjectId optional allowed object id on goal
     * @return path excluding start and including goal, or null
     */
    public List<Position> findPath(Position start, Position goal, Integer allowedObjectId) {
        return pathFinder.findPath(this, start, goal, allowedObjectId);
    }

    /**
     * Gets object by id.
     *
     * @param objectId object id
     * @return object or null
     */
    public FactoryObject getObjectById(int objectId) {
        return objects.get(objectId);
    }

    /**
     * Executes one simulation step command output and advances one iteration.
     */
    public void runStep() {
        for (Robot robot : robots) {
            robot.tick(this);
        }
        stepCount++;
        printState();
    }

    /**
     * Prints current simulation state without advancing iteration.
     */
    public void printCurrentState() {
        printState();
    }

    /**
     * Adds a user task after validation.
     *
     * @param objectId object id
     * @param destination destination position
     */
    public void addTask(int objectId, Position destination) {
        if (!objects.containsKey(objectId)) {
            System.out.println("Object with ID " + objectId + " not found.");
            return;
        }

        if (!isValidDestination(destination)) {
            System.out.println("Invalid destination.");
            return;
        }

        for (Task task : tasks) {
            if (task.getObjectId() == objectId && task.getStatus() != TaskStatus.COMPLETED) {
                System.out.println("Object already associated to a task.");
                return;
            }
        }

        tasks.add(new Task(objectId, destination));
    }

    /**
     * Prints robot information for a given id.
     *
     * @param robotId robot id
     */
    public void printRobot(int robotId) {
        Robot robot = findRobotById(robotId);
        if (robot == null) {
            System.out.println("Robot with ID " + robotId + " not found.");
            return;
        }
        System.out.println(robot.toStatusLine());
    }

    /**
     * Returns first pending feasible assignment for a robot.
     *
     * @param robot robot
     * @return assignment or null
     */
    public TaskAssignment findAssignmentForRobot(Robot robot) {
        for (Task task : tasks) {
            if (task.getStatus() != TaskStatus.PENDING) {
                continue;
            }

            TaskPlan plan = computeTaskPlan(robot, task);
            if (plan != null) {
                return new TaskAssignment(task, plan);
            }
        }
        return null;
    }

    private TaskPlan computeTaskPlan(Robot robot, Task task) {
        FactoryObject object = objects.get(task.getObjectId());
        if (object == null || object.isCarried()) {
            return null;
        }

        Position objectPosition = object.getPosition();
        Position destination = task.getDestination();

        List<Position> pathToObject = findPath(robot.getPosition(), objectPosition, object.getId());
        if (pathToObject == null) {
            return null;
        }

        List<Position> pathToDestination = findPath(objectPosition, destination, object.getId());
        if (pathToDestination == null) {
            return null;
        }

        List<Position> pathToStation = findPath(destination, robot.getChargingStation(), null);
        if (pathToStation == null) {
            return null;
        }

        TaskPlan plan = new TaskPlan(pathToObject, pathToDestination, pathToStation);
        double expectedChargeAfterWorstCase = robot.getCharge() - (plan.getTotalMoves() * 0.5);
        if (expectedChargeAfterWorstCase < 0.0) {
            return null;
        }

        return plan;
    }

    private void printState() {
        System.out.println("Step count: " + stepCount);
        printMap();
        System.out.println();
        System.out.println("ROBOTS");
        for (Robot robot : robots) {
            System.out.println(robot.toStatusLine());
        }
        System.out.println();
        System.out.println("TASKS");
        for (Task task : tasks) {
            if (task.getStatus() == TaskStatus.COMPLETED) {
                continue;
            }
            if (task.getStatus() == TaskStatus.PENDING) {
                System.out.println(task.toTaskLabel() + " - PENDING");
            } else {
                System.out.println(task.toTaskLabel() + " - ASSIGNED TO R" + task.getAssignedRobotId());
            }
        }
        System.out.println();
    }

    private void printMap() {
        StringBuilder header = new StringBuilder("   ");
        for (int x = 1; x <= width; x++) {
            if (x > 1) {
                header.append(' ');
            }
            header.append(String.format("%02d", x));
        }
        System.out.println(header);

        for (int y = 1; y <= height; y++) {
            StringBuilder line = new StringBuilder();
            line.append(String.format("%02d ", y));
            for (int x = 1; x <= width; x++) {
                if (x > 1) {
                    line.append(' ');
                }
                Position pos = new Position(x, y);
                line.append(getCellToken(pos));
            }
            System.out.println(line);
        }
    }

    private String getCellToken(Position pos) {
        int robotsOnCell = 0;
        Robot singleRobot = null;
        for (Robot robot : robots) {
            if (robot.getPosition().equals(pos)) {
                robotsOnCell++;
                singleRobot = robot;
            }
        }

        if (robotsOnCell > 1) {
            return "RR";
        }

        if (singleRobot != null) {
            if (singleRobot.isChargingOnStation() && singleRobot.getChargingStation().equals(pos)) {
                return "C" + singleRobot.getId();
            }
            return "R" + singleRobot.getId();
        }

        if (obstacles.contains(pos)) {
            return "##";
        }

        for (Robot robot : robots) {
            if (robot.getChargingStation().equals(pos)) {
                return "S" + robot.getId();
            }
        }

        for (FactoryObject object : objects.values()) {
            if (!object.isCarried() && object.getPosition().equals(pos)) {
                return "O" + object.getId();
            }
        }

        return "..";
    }

    private boolean isValidDestination(Position destination) {
        if (!isInside(destination)) {
            return false;
        }

        if (obstacles.contains(destination)) {
            return false;
        }

        for (Robot robot : robots) {
            if (robot.getChargingStation().equals(destination)) {
                return false;
            }
        }

        for (FactoryObject object : objects.values()) {
            if (!object.isCarried() && object.getPosition().equals(destination)) {
                return false;
            }
        }

        return true;
    }

    private Robot findRobotById(int robotId) {
        for (Robot robot : robots) {
            if (robot.getId() == robotId) {
                return robot;
            }
        }
        return null;
    }
}
