// classe central - parte principal do sistema

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe central do sistema de simulação, responsável por gerir o estado global do projeto.
 *
 * <p>Agrega a grelha de simulação ({@link InitGrid}), a lista de robots ({@link Robot})
 * e a lista de tarefas ({@link Task}), coordenando a interação entre todos através
 * de um loop de comandos interativo.</p>
 *
 * <p>Comandos disponíveis:</p>
 * <ul>
 *   <li>{@code step} - avança um passo da simulação</li>
 *   <li>{@code add-task} - cria uma nova tarefa</li>
 *   <li>{@code get-robot} - mostra o estado de um robot</li>
 *   <li>{@code help} - lista os comandos disponíveis</li>
 *   <li>{@code exit} - termina a simulação</li>
 * </ul>
 *
 * @see Robot
 * @see Task
 * @see InitGrid
 */
public class Project {
    private int stepCount;
    private List<Robot> robots;
    private List<Task> tasks;
    private InitGrid initGrid;

    /**
     * Construtor da Classe que inicializa o projeto com a grelha e os robots fornecidos.
     *
     * @param initGrid  grelha inicial da simulação
     * @param robots    lista de robots que vão operar na grelha
     */
    public Project(InitGrid initGrid, List<Robot> robots) {
        this.stepCount = 0;
        this.initGrid = initGrid;
        this.robots = robots;
        this.tasks = new ArrayList<>();
    }

    /**
     * Getters de InitGrid, Tasks e Robots, respetivamente.
     * @return
     */

    public InitGrid getInitGrid() {
        return this.initGrid;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public List<Robot> getRobots() {
        return this.robots;
    }

    /**
     * Método startProject, que inicia o loop interativo da simulação, lendo e processando comandos do utilizador.
     *
     * <p>Comandos suportados:</p>
     * <ul>
     *   <li>{@code help} - mostra a lista de comandos</li>
     *   <li>{@code step} - executa um passo e atualiza a interface</li>
     *   <li>{@code add-task <objId> <destX> <destY>} - cria uma nova tarefa</li>
     *   <li>{@code get-robot <id>} - mostra o estado do robot com o id indicado</li>
     *   <li>{@code exit} - termina o loop e encerra a simulação</li>
     * </ul>
     *
     * @param scanner   Scanner para leitura dos comandos do utilizador
     */
    public void startProject(Scanner scanner) {
        boolean active = true;

        printInterface();

        while (active == true) {
            System.out.print("Enter command: ");
            String option = scanner.nextLine().trim();

            if (option.isEmpty()) {
                continue;
            }

            String[] parts = option.split(" ");
            String command = parts[0].toLowerCase();

            switch (command) {
                case "help":
                    if (parts.length != 1) {
                        System.out.println("Unknown utility: " + parts[1] + ".");
                    } else {
                        printHelp();
                    }
                    break;

                case "step":
                    stepCount++;
                    executeStep();
                    printInterface();
                    break;

                case "add-task":
                    if (parts.length != 4) {
                        System.out.println("Invalid number of arguments.");
                        break;
                    }
                    try {
                        int objId = Integer.parseInt(parts[1]);
                        int endX = Integer.parseInt(parts[2]);
                        int endY = Integer.parseInt(parts[3]);
                        newTask(objId, endX, endY);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number of arguments.");
                    }
                    break;

                case "get-robot":
                    if (parts.length != 2) {
                        System.out.println("Invalid number of arguments.");
                        break;
                    }
                    try {
                        int robotId = Integer.parseInt(parts[1]);
                        showRobot(robotId);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number of arguments.");
                    }
                    break;

                case "exit":
                    active = false;
                    break;

                default:
                    System.out.println("Unknown utility: " + command + ".");
                    break;
            }
        }
    }

    /**
     * Método executeStep, que avança a simulação um passo, chamando {@link Robot#executeStep} em cada robot.
     */
    private void executeStep() {
        for (Robot robot : robots) {
            robot.executeStep(this);
        }
    }

    /**
     * Método newTask, que cria uma nova tarefa para o objeto indicado, validando a sua existência
     * na grelha e a validade do destino antes de a adicionar à lista.
     *
     * @param objId     id do objeto a transportar
     * @param endX      coordenada X do destino
     * @param endY      coordenada Y do destino
     */
    private void newTask(int objId, int endX, int endY) {
        Position objectPosition = findObjectPosition(objId);
        if (objectPosition == null) {
            System.out.println("Object with ID " + objId + " not found.");
            return;
        }

        if (!isValidDestination(endX, endY)) {
            System.out.println("Invalid destination.");
            return;
        }

        Position finalPosition = new Position(endX, endY);
        Task task = new Task(objId, objectPosition, finalPosition);
        this.tasks.add(task);
        System.out.println("Task created");
    }

    /**
     * Método findObjectPosition, que percorre a grelha à procura do objeto com o id indicado
     * e retorna a sua posição, ou {@code null} se não for encontrado.
     *
     * @param objId     id do objeto a localizar
     * @return          posição do objeto na grelha, ou {@code null} se não existir
     */
    private Position findObjectPosition(int objId) {
        String target = "O" + objId;

        for (int y = 0; y < initGrid.getHeight(); y++) {
            for (int x = 0; x < initGrid.getWidth(); x++) {
                if (target.equals(initGrid.getGridEntity(y, x))) {
                    return new Position(x + 1, y + 1);
                }
            }
        }

        return null;
    }

    /**
     * Método showRobot, que imprime o estado do robot com o id indicado.
     * Se não for encontrado, imprime mensagem de erro.
     *
     * @param robotId   id do robot a mostrar
     */
    private void showRobot(int robotId) {
        for (Robot robot : robots) {
            if (robot.getId() == robotId) {
                System.out.println(robot);
                return;
            }
        }
        System.out.println("Robot with ID " + robotId + " not found.");
    }

    private void printHelp() {
        System.out.println("[Help - List of commands]");
        System.out.println("help - Show list of commands");
        System.out.println("step - Run next simulation step");
        System.out.println("add-task <obj-id> <dest-x> <dest-y> - Add new task");
        System.out.println("get-robot <id> - Get robot information");
        System.out.println("exit - Exit the simulation");
    }

    private void printInterface() {
        System.out.println("Step count: " + stepCount);

        int width = initGrid.getWidth();
        int height = initGrid.getHeight();

        System.out.print("  ");
        for (int x = 1; x <= width; x++) {
            System.out.printf(" %02d", x);
        }
        System.out.println();

        for (int y = 1; y <= height; y++) {
            System.out.printf("%02d", y);
            for (int x = 1; x <= width; x++) {
                String cell = cellAt(x, y);
                System.out.print(" " + cell);
            }
            System.out.println();
        }
        System.out.println("");
        System.out.println("ROBOTS");
        for (int i = 0; i < robots.size(); i++) {
            System.out.println(robots.get(i));
        }
        System.out.println("");
        System.out.println("TASKS");
        System.out.println("");
        for (int ti = 0; ti < tasks.size(); ti++) {
            Task t = tasks.get(ti);
            String owner = "";
            for (int ri = 0; ri < robots.size(); ri++) {
                Robot r = robots.get(ri);
                if (r.getCurrentTask() == t) {
                    owner = " - ASSIGNED TO R" + r.getId();
                    break;
                }
            }
            System.out.println("(O" + t.getObjectId() + " --> " + t.getEnd().getX() + ";" + t.getEnd().getY() + ")" + owner);
        }
    }

    private String cellAt(int x, int y) {
        for (int i = 0; i < robots.size(); i++) {
            Robot r = robots.get(i);
            if (r.getPosition().getX() == x && r.getPosition().getY() == y) {
                return "R" + r.getId();
            }
        }
        return initGrid.getGridEntity(y - 1, x - 1);
    }

    private boolean isValidDestination(int x, int y) {
        if (x < 1 || y < 1 || x > initGrid.getWidth() || y > initGrid.getHeight()) {
            return false;
        }
        return "..".equals(initGrid.getGridEntity(y - 1, x - 1));
    }
}