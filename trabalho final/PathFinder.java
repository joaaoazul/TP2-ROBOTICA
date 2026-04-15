import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements depth-first search (DFS) path finding with left-up-right-down order.
 */
public class PathFinder {
    private static final int[][] DIRECTIONS = {
        {-1, 0},
        {0, -1},
        {1, 0},
        {0, 1}
    };

    /**
     * Finds a path using DFS from start to goal.
     *
     * @param map simulation map
     * @param start start position
     * @param goal goal position
     * @param allowedObjectId object id allowed as target cell, or null
     * @return path excluding start and including goal; empty when start equals goal; null when impossible
     */
    public List<Position> findPath(Simulation map, Position start, Position goal, Integer allowedObjectId) {
        if (start.equals(goal)) {
            return new ArrayList<>();
        }

        List<NodeState> stack = new ArrayList<>();
        List<Position> path = new ArrayList<>();
        Set<Position> visited = new HashSet<>();

        stack.add(new NodeState(start));
        path.add(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            NodeState current = stack.get(stack.size() - 1);
            Position currentPosition = current.position;

            if (currentPosition.equals(goal)) {
                List<Position> result = new ArrayList<>(path);
                result.remove(0);
                return result;
            }

            Position next = null;
            while (current.nextDirectionIndex < DIRECTIONS.length && next == null) {
                int[] dir = DIRECTIONS[current.nextDirectionIndex++];
                Position candidate = currentPosition.move(dir[0], dir[1]);

                if (!map.isInside(candidate)) {
                    continue;
                }
                if (visited.contains(candidate)) {
                    continue;
                }
                if (!map.isAccessibleForPath(candidate, goal, allowedObjectId)) {
                    continue;
                }
                next = candidate;
            }

            if (next != null) {
                stack.add(new NodeState(next));
                visited.add(next);
                path.add(next);
            } else {
                stack.remove(stack.size() - 1);
                path.remove(path.size() - 1);
            }
        }

        return null;
    }

    /**
     * Stores DFS state for one position.
     */
    private static class NodeState {
        private final Position position;
        private int nextDirectionIndex;

        private NodeState(Position position) {
            this.position = position;
            this.nextDirectionIndex = 0;
        }
    }
}
