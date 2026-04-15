/**
 * Represents a movable object in the installation.
 */
public class FactoryObject {
    private final int id;
    private Position position;
    private boolean carried;

    /**
     * Creates a new object.
     *
     * @param id object id
     * @param position object position
     */
    public FactoryObject(int id, Position position) {
        this.id = id;
        this.position = position;
        this.carried = false;
    }

    /**
     * Gets object id.
     *
     * @return object id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets object position.
     *
     * @return position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets object position.
     *
     * @param position new position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Indicates if object is currently carried by a robot.
     *
     * @return true if carried
     */
    public boolean isCarried() {
        return carried;
    }

    /**
     * Sets carry state.
     *
     * @param carried carry state
     */
    public void setCarried(boolean carried) {
        this.carried = carried;
    }
}
