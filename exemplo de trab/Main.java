import java.util.Scanner;

/**
 * Program entry point.
 */
public class Main {
    /**
     * Starts the application by reading initial data and launching the simulation.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InputParser parser = new InputParser();

        Simulation simulation = parser.parseSimulation(scanner);
        if (simulation == null) {
            return;
        }

        simulation.printCurrentState();

        CommandLoop commandLoop = new CommandLoop(simulation, scanner);
        commandLoop.run();
    }
}
