import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Parses initial input and creates the simulation instance.
 */
public class InputParser {
    /**
     * Parses dimensions and initial entities from standard input.
     *
     * @param scanner scanner for console input
     * @return simulation instance or null when validation fails
     */
    public Simulation parseSimulation(Scanner scanner) {
        while (true) {
            System.err.println("[Setup] Introduza as dimensões no formato: <largura> <altura>");

            String dimensionsLine = readNextNonEmptyLine(scanner);
            if (dimensionsLine == null) {
                return null;
            }

            String[] dimensionsTokens = dimensionsLine.split("\\s+");
            if (dimensionsTokens.length != 2 || !isInteger(dimensionsTokens[0]) || !isInteger(dimensionsTokens[1])) {
                System.out.println("Invalid dimensions format.");
                continue;
            }

            int width = Integer.parseInt(dimensionsTokens[0]);
            int height = Integer.parseInt(dimensionsTokens[1]);

            if (width < 5 || height < 5) {
                System.out.println("Minimum dimensions allowed is 5 by 5.");
                continue;
            }

            if (width > 99 || height > 99) {
                System.out.println("Maximum dimensions allowed is 99 by 99.");
                continue;
            }

            System.err.println("[Setup] Introduza entidades (OBS/ROB/OBJ) e termine com 'start'.");

            Set<Position> occupied = new HashSet<>();
            Set<Position> obstacles = new HashSet<>();
            List<Robot> robots = new ArrayList<>();
            Map<Integer, FactoryObject> objects = new HashMap<>();

            int entityIndex = 0;
            boolean restartSetup = false;

            while (true) {
                String line = readNextNonEmptyLine(scanner);
                if (line == null) {
                    return null;
                }

                if ("start".equalsIgnoreCase(line)) {
                    break;
                }

                entityIndex++;
                String[] tokens = line.split("\\s+");
                String type = tokens[0];

                if (!"OBS".equals(type) && !"ROB".equals(type) && !"OBJ".equals(type)) {
                    System.out.println("Unknown entity type \"" + type + "\" at entity " + entityIndex + ".");
                    restartSetup = true;
                    break;
                }

                if ("OBS".equals(type)) {
                    if (tokens.length != 3 || !isInteger(tokens[1]) || !isInteger(tokens[2])) {
                        System.out.println("Invalid entity format at entity " + entityIndex + ".");
                        restartSetup = true;
                        break;
                    }
                    Position obstaclePosition = new Position(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                    if (!isValidEntityPosition(obstaclePosition, width, height, occupied)) {
                        System.out.println("Invalid entity coordinates at entity " + entityIndex + ".");
                        restartSetup = true;
                        break;
                    }
                    obstacles.add(obstaclePosition);
                    occupied.add(obstaclePosition);
                    continue;
                }

                if ("OBJ".equals(type)) {
                    if (tokens.length != 3 || !isInteger(tokens[1]) || !isInteger(tokens[2])) {
                        System.out.println("Invalid entity format at entity " + entityIndex + ".");
                        restartSetup = true;
                        break;
                    }
                    Position objectPosition = new Position(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                    if (!isValidEntityPosition(objectPosition, width, height, occupied)) {
                        System.out.println("Invalid entity coordinates at entity " + entityIndex + ".");
                        restartSetup = true;
                        break;
                    }
                    int objectId = objects.size() + 1;
                    objects.put(objectId, new FactoryObject(objectId, objectPosition));
                    occupied.add(objectPosition);
                    continue;
                }

                if (tokens.length != 5 || !isInteger(tokens[1]) || !isInteger(tokens[2])
                    || !isInteger(tokens[3]) || !isInteger(tokens[4])) {
                    System.out.println("Invalid entity format at entity " + entityIndex + ".");
                    restartSetup = true;
                    break;
                }

                Position robotPosition = new Position(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                Position stationPosition = new Position(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));

                if (!isValidEntityPosition(robotPosition, width, height, occupied)
                    || !isValidEntityPosition(stationPosition, width, height, occupied)
                    || robotPosition.equals(stationPosition)) {
                    System.out.println("Invalid entity coordinates at entity " + entityIndex + ".");
                    restartSetup = true;
                    break;
                }

                int robotId = robots.size() + 1;
                robots.add(new Robot(robotId, robotPosition, stationPosition));
                occupied.add(robotPosition);
                occupied.add(stationPosition);
            }

            if (restartSetup) {
                continue;
            }

            if (entityIndex == 0) {
                System.out.println("Expected entity name.");
                continue;
            }

            if (robots.isEmpty() || objects.isEmpty()) {
                System.out.println("At least 1 robot and 1 object are required.");
                continue;
            }

            return new Simulation(width, height, obstacles, robots, objects);
        }
    }

    private String readNextNonEmptyLine(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
        }
        return null;
    }

    private boolean isValidEntityPosition(Position position, int width, int height, Set<Position> occupied) {
        if (position.getX() < 1 || position.getX() > width || position.getY() < 1 || position.getY() > height) {
            return false;
        }
        return !occupied.contains(position);
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
