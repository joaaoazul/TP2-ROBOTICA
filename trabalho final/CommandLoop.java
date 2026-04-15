import java.util.Locale;
import java.util.Scanner;

/**
 * Handles simulation command loop.
 */
public class CommandLoop {
    private final Simulation simulation;
    private final Scanner scanner;

    /**
     * Creates a command loop.
     *
     * @param simulation simulation instance
     * @param scanner input scanner
     */
    public CommandLoop(Simulation simulation, Scanner scanner) {
        this.simulation = simulation;
        this.scanner = scanner;
    }

    /**
     * Runs command processing until exit.
     */
    public void run() {
        while (true) {
            System.out.print("Enter command: ");
            System.out.flush();
            if (!scanner.hasNextLine()) {
                System.err.println("[Info] Fim de input. Programa terminado.");
                return;
            }

            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            try {
                String[] tokens = line.split("\\s+");
                String command = tokens[0].toLowerCase(Locale.ROOT);

                if ("help".equals(command)) {
                    printHelp();
                    continue;
                }

                if ("step".equals(command)) {
                    simulation.runStep();
                    continue;
                }

                if ("add-task".equals(command)) {
                    handleAddTask(tokens);
                    continue;
                }

                if ("get-robot".equals(command)) {
                    handleGetRobot(tokens);
                    continue;
                }

                if ("exit".equals(command)) {
                    System.err.println("[Info] Simulação terminada.");
                    return;
                }

                System.out.println("Invalid command.");
            } catch (Exception ex) {
                System.err.println("[Erro] Ocorreu um erro ao processar o comando. Tenta novamente.");
            }
        }
    }

    private void printHelp() {
        System.out.println("[Help - List of commands]");
        System.out.println("help - Show list of commands");
        System.out.println("step - Run next simulation step");
        System.out.println("add-task <obj-id> <dest-x> <dest-y> - Add new task");
        System.out.println("get-robot <id> - Get robot information");
        System.out.println("exit - Exit the simulation");
    }

    private void handleAddTask(String[] tokens) {
        if (tokens.length != 4 || !isInteger(tokens[1]) || !isInteger(tokens[2]) || !isInteger(tokens[3])) {
            System.out.println("Invalid command.");
            return;
        }

        int objectId = Integer.parseInt(tokens[1]);
        int destinationX = Integer.parseInt(tokens[2]);
        int destinationY = Integer.parseInt(tokens[3]);

        simulation.addTask(objectId, new Position(destinationX, destinationY));
    }

    private void handleGetRobot(String[] tokens) {
        if (tokens.length != 2 || !isInteger(tokens[1])) {
            System.out.println("Invalid command.");
            return;
        }
        simulation.printRobot(Integer.parseInt(tokens[1]));
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
