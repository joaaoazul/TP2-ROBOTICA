/**
 * Represents an object transport task.
 */
public class Task {
    private final int objectId;
    private final Position destination;
    private TaskStatus status;
    private Integer assignedRobotId;

    /**
     * Creates a task.
     *
     * @param objectId object id
     * @param destination destination position
     */
    public Task(int objectId, Position destination) {
        this.objectId = objectId;
        this.destination = destination;
        this.status = TaskStatus.PENDING;
        this.assignedRobotId = null;
    }

    /**
     * Gets object id.
     *
     * @return object id
     */
    public int getObjectId() {
        return objectId;
    }

    /**
     * Gets destination.
     *
     * @return destination
     */
    public Position getDestination() {
        return destination;
    }

    /**
     * Gets task status.
     *
     * @return status
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Sets task status.
     *
     * @param status task status
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Gets assigned robot id.
     *
     * @return robot id or null
     */
    public Integer getAssignedRobotId() {
        return assignedRobotId;
    }

    /**
     * Sets assigned robot id.
     *
     * @param assignedRobotId robot id
     */
    public void setAssignedRobotId(Integer assignedRobotId) {
        this.assignedRobotId = assignedRobotId;
    }

    /**
     * Returns task label in expected format.
     *
     * @return task label
     */
    public String toTaskLabel() {
        return "(O" + objectId + " --> " + destination.toCoordinateString() + ")";
    }
}
