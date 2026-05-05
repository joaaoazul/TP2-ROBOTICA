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
            System.out.print("Enter Command: ");
            String option = scanner.nextLine().trim();

            if (option.isEmpty()){ //se der so enter é so voltar ao inicio
                continue;
            }

            //tratamento de dados da opção escolhida
            String[] parts = option.split(" ");
            String command = parts[0].toLowerCase();

            switch (command) {
                case "help":
                    printHelp();
                    break;

                case "step":
                    stepCount++;
                    executeStep();
                    printInterface();
                    break;

                case "add-task":
                    if (parts.length !=4){
                        System.out.println("ERROR! you can consult the help command");
                        break;
                    }
                    try { // atribuimos valores a 3 variaveis tirado do comando previamente dividido em partes sabendo que a parte 0 vai ser o comando add-task
                        int objId = Integer.parseInt(parts[1]);
                        int endX = Integer.parseInt(parts[2]);
                        int endY = Integer.parseInt(parts[3]);
                        newTask(objId, endX, endY);
                    } catch (NumberFormatException e) {
                        System.out.println("Put correct format");
                    }
                    break;

                case "get-robot":
                    if (parts.length != 2){
                        System.out.println("ERROR!  you can consult the help command");
                        break;
                    }
                    try {
                        int robotId = Integer.parseInt(parts[1]);
                        showRobot(robotId);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID");
                    }
                    break;

                case "exit":
                    active = false;
                    break;

                default:
                    System.out.println("Unknown type");
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
            System.out.println("Object not found");
            return;
        }

        Position finalPosition = new Position(endX, endY);
        Task task = new Task(objId, objectPosition, finalPosition);
        this.tasks.add(task);
        System.out.println("Task created");
    }

    private Position findObjectPosition(int objId) { //???????
        String target = "O" + objId;

        for (int y = 0; y < initGrid.getHeight(); y++) {
            for (int x = 0; x < initGrid.getWidth(); x++) {
                if (target.equals(initGrid.getGridEntity(x, y))) {
                    return new Position(x, y);
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
        System.out.println("Robot not found");
    }

    private void printHelp(){
        System.out.println("[Help – List of commands]");
        System.out.println("help - Show list of commands");
        System.out.println("step – Run next simulation step");
        System.out.println("add-task <obj-id> <dest-x> <dest-y> – Add new task");
        System.out.println("get-robot <id> - Get robot information");
        System.out.println("exit – Exit the simulation");

    }

    //???
    private void printInterface() {
        
        System.out.println("Step count: " + stepCount);

        int width = initGrid.getWidth();
        int height = initGrid.getHeight();

        System.out.print(" ");
        for (int x = 1; x <= width; x++) {
            System.out.printf(" %02d", x);
        }
        System.out.println();

        for (int y = 1; y <= height; y++) {
            System.out.print(String.format("%02d ", y));
            for (int x = 1; x <= width; x++) {
                System.out.print(" " + cellAt(x, y));
            }
            System.out.println();
        }

        System.out.println("ROBOTS");
        for (int i = 0; i < robots.size(); i++) {
            Robot r = robots.get(i);
            String taskStr = "NONE";
            if (r.getCurrentTask() != null) {
                Task tt = r.getCurrentTask();
                taskStr = "(O" + tt.getObjectId() + " --> " + tt.getEnd().getX() + ";" + tt.getEnd().getY() + ")";
            }
            System.out.println("[R" + r.getId() + "] pos=" + r.getPosition().getX() + ";" + r.getPosition().getY()
                    + " charge=" + String.format("%.2f", r.getCharge()) + "% state=" + r.getState() + " task=" + taskStr);
        }

        System.out.println("TASKS");
        for (int ti = 0; ti < tasks.size(); ti++) {
            Task t = tasks.get(ti);
            String owner = "";
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
    }

    private String cellAt(int x, int y) {
        for (int i = 0; i < robots.size(); i++) {
            Robot r = robots.get(i);
            if (r.getPosition().getX() == x && r.getPosition().getY() == y) {
                return "R" + r.getId();
            }
        }
        String cell = initGrid.getGridEntity(x - 1, y - 1);
        return cell;
    }
    
}
