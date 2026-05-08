// classe central - parte principal do sistema 

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Project {
    private int stepCount;
    private List<Robot> robots;
    private List<Task> tasks;
    private InitGrid initGrid;

    public Project(InitGrid initGrid, List<Robot> robots) {
        this.stepCount = 0; //a contagem de iterações começa a 0 
        this.initGrid = initGrid;
        this.robots = robots;
        this.tasks = new ArrayList<>(); //começa com a lista de tarefas vazia
    }

    public InitGrid getInitGrid() {
        return this.initGrid;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public List<Robot> getRobots() {
        return this.robots;
    }
    
    public void startProject(Scanner scanner) {
        boolean active = true;

        // mostra a interface
        printInterface();

        while (active == true){
            System.out.print("Enter command: ");
            String option = scanner.nextLine().trim();

            if (option.isEmpty()){ //se der so enter é so voltar ao inicio
                continue;
            }

            //tratamento de dados da opção escolhida
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
                    if (parts.length != 1) {
                        System.out.println("Invalid number of arguments.");
                        break;
                    }
                    stepCount++;
                    executeStep();
                    printInterface();
                    break;

                case "add-task":
                    if (parts.length != 4){
                        System.out.println("Invalid number of arguments.");
                        break;
                    }
                    try { // atribuimos valores a 3 variaveis tirado do comando previamente dividido em partes sabendo que a parte 0 vai ser o comando add-task
                        int objId = Integer.parseInt(parts[1]);
                        int endX = Integer.parseInt(parts[2]);
                        int endY = Integer.parseInt(parts[3]);
                        newTask(objId, endX, endY);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number of arguments.");
                    }
                    break;

                case "get-robot":
                    if (parts.length != 2){
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

    private void executeStep() {
        for (Robot robot : robots) {
            robot.executeStep(this);
        }
    }

    private void newTask(int objId, int endX, int endY) {
        Position objectPosition = findObjectPosition(objId);
        if (objectPosition == null) {
            System.out.println("Object with ID " + objId + " not found.");
            return;
        }

        for (int i = 0; i < this.tasks.size(); i++) {
            if (this.tasks.get(i).getObjectId() == objId && this.tasks.get(i).getStatus() != TaskStatus.COMPLETED) {
                System.out.println("Object already associated to a task.");
                return;
            }
        }

        if (!isValidDestination(endX, endY)) {
            System.out.println("Invalid destination.");
            return;
        }

        Position finalPosition = new Position(endX, endY);
        Task task = new Task(objId, objectPosition, finalPosition);
        this.tasks.add(task);
    }

    private Position findObjectPosition(int objId) { //???????
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

    private void showRobot(int robotId){
        for (Robot robot : robots) {
            if(robot.getId() == robotId) {
                System.out.println(robot);
                return;
            }
        }
        System.out.println("Robot with ID " + robotId + " not found.");
    }

    private void printHelp(){
        System.out.println("[Help - List of commands]");
        System.out.println("help - Show list of commands");
        System.out.println("step - Run next simulation step");
        System.out.println("add-task <obj-id> <dest-x> <dest-y> - Add new task");
        System.out.println("get-robot <id> - Get robot information");
        System.out.println("exit - Exit the simulation");

    }

    //???
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
        for (int ti = 0; ti < tasks.size(); ti++) {
            Task t = tasks.get(ti);
            if (t.getStatus() == TaskStatus.COMPLETED) {
                continue; //tarefas concluidas nao sao mostradas
            }
            String owner = " - AVAILABLE";
            // find owner (simple loop)
            for (int ri = 0; ri < robots.size(); ri++) {
                Robot r = robots.get(ri);
                if (r.getCurrentTask() == t) {
                    owner = " - ASSIGNED TO R" + r.getId();
                    break;
                }
            }
            System.out.println("(O" + t.getObjectId() + " --> " + t.getEnd().getX() + ";" + t.getEnd().getY() + ")" + owner);
        }
        System.out.println("");
    }

    private String cellAt(int x, int y) {
        for (int i = 0; i < robots.size(); i++) {
            Robot r = robots.get(i);
            if (r.getPosition().getX() == x && r.getPosition().getY() == y) {
                if (r.getState() == RobotState.CHARGING) {
                    return "C" + r.getId();
                }
                return "R" + r.getId();
            }
        }
        String cell = initGrid.getGridEntity(y - 1, x - 1);
        //a grelha ainda tem a posicao inicial dos robos, se nenhum robo la esta entao ja saiu
        if (cell.startsWith("R")) {
            return "..";
        }
        //se o objeto foi apanhado por algum robo, ja nao esta na posicao original
        if (cell.startsWith("O")) {
            try {
                int objId = Integer.parseInt(cell.substring(1));
                for (int i = 0; i < robots.size(); i++) {
                    InitObject co = robots.get(i).getCaughtObject();
                    if (co != null && co.getId() == objId) {
                        return "..";
                    }
                }
            } catch (NumberFormatException e) {
                //ignora
            }
        }
        return cell;
    }

    private boolean isValidDestination(int x, int y) {
        if (x < 1 || y < 1 || x > initGrid.getWidth() || y > initGrid.getHeight()) {
            return false;
        }

        return "..".equals(initGrid.getGridEntity(y - 1, x - 1));
    }
    
}
