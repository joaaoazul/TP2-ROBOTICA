import java.util.List;

/**
 * Stores pre-computed paths required for a robot to execute a task.
 */
public class TaskPlan {
    private final List<Position> pathToObject;
    private final List<Position> pathToDestination;
    private final List<Position> pathToStation;

    /**
     * Creates a task plan.
     *
     * @param pathToObject path robot->object
     * @param pathToDestination path object->destination
     * @param pathToStation path destination->station
     */
    public TaskPlan(List<Position> pathToObject, List<Position> pathToDestination, List<Position> pathToStation) {
        this.pathToObject = pathToObject;
        this.pathToDestination = pathToDestination;
        this.pathToStation = pathToStation;
    }

    /**
     * Gets path robot->object.
     *
     * @return path
     */
    public List<Position> getPathToObject() {
        return pathToObject;
    }

    /**
     * Gets path object->destination.
     *
     * @return path
     */
    public List<Position> getPathToDestination() {
        return pathToDestination;
    }

    /**
     * Gets path destination->station.
     *
     * @return path
     */
    public List<Position> getPathToStation() {
        return pathToStation;
    }

    /**
     * Gets total move count of all paths.
     *
     * @return move count
     */
    public int getTotalMoves() {
        return pathToObject.size() + pathToDestination.size() + pathToStation.size();
    }
}
