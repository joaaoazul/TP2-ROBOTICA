import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            InputReader input = new InputReader(scanner);
            
            Project project = input.projectsEngine();
        
            if (project != null) {
                project.startProject(scanner);
            }
        }
    }
}