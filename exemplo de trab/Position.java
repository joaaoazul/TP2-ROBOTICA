import java.util.Objects;

/**
 * Represents a position in the installation map.
 */
public final class Position {
    private final int x;
    private final int y;

    /**
     * Creates a position.
     *
     * @param x x coordinate (column)
     * @param y y coordinate (row)
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x coordinate.
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns a new position moved by delta.
     *
     * @param dx delta x
     * @param dy delta y
     * @return new position
     */
    public Position move(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    /**
     * Returns position in x;y format.
     *
     * @return coordinate string
     */
    public String toCoordinateString() {
        return x + ";" + y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Position)) {
            return false;
        }
        Position other = (Position) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
