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
                    this.stepCount = stepCount + 1;
                    //falta a logica
                    break;

                case "add-task":
                    break;

                case "get-robot":
                    break;

                case "exit":
                    active = false;
                    break;

                default:
                    System.out.println("Unknown command");
                    break;

            }
        }
    }
    

    

    private void printHelp(){
        System.out.println("[Help – List of commands]");
        System.out.println("help - Show list of commands");
        System.out.println("step – Run next simulation step");
        System.out.println("add-task <obj-id> <dest-x> <dest-y> – Add new task");
        System.out.println("get-robot <id> - Get robot information");
        System.out.println("exit – Exit the simulation");

    }
}
