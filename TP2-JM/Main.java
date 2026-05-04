import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

       Scanner scanner = new Scanner(System.in);
    
       InputReader input = new InputReader();

       Project project = input.initRead(scanner);
        
       if (project != null) {
        project.startProject(scanner);
       }

       scanner.close();


        
    }

}
