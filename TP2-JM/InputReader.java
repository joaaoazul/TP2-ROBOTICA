import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// Valida formato das entidades e requisitos mínimos
// Validações de coordenadas e dimensões ficam no InitGrid

public class InputReader {

    private Scanner scanner = new Scanner(System.in);
    private List<Obstacle> obstacles = new ArrayList<>();
    private List<Robot> robots = new ArrayList<>();
    private List<InitObject> objects = new ArrayList<>();

    public InputReader(Scanner scanner){
        this.scanner = scanner;
    }

    //métodos principais da classe

    //readProject é o único público porque vai ser chamado de fora
    public Project projectsEngine(){
        
        int[] dimensions = dimensionsEngine();
        if (dimensions == null) {
            System.exit(0);
        }
      
      int width = dimensions[0];
      int height = dimensions[1];

      entityEngine(width, height);

         if (robots.isEmpty() || objects.isEmpty()) {
        System.exit(0);
    }
    return new Project(width, height, obstacles, robots, objects);

    }

    private int[] dimensionsEngine(){
        //recebe os números em string, separa-os e guarda nesse array
        String line = scanner.nextLine();
        String[] input = line.trim().split(" ");
        if(input.length < 2){
            System.out.println("Invalid dimensions format.");
            return null;
        }

        //tenta converter para inteiro e verifica dimensões, se não der, manda erro
        try{
            int width = Integer.parseInt(input[0]);
            int height = Integer.parseInt(input[1]);

            if (width <= 5 || height <= 5) {
                System.out.println("Minimum dimensions allowed is 5 by 5.");
                return null;
            }
            if (width >= 99 || height >= 99) {
                System.out.println("Maximum dimensions allowed is 99 by 99.");
                return null;
            }
            return new int[]{width, height};
        } catch (NumberFormatException e) {
            System.out.println("Invalid dimensions.");
            return null;
        }
    }

    private void entityEngine(int width, int height){
        int entityCounter = 0;
        int robotCounter = 0;
        int objectCounter = 0;

        while(true) {
            String inputReader = scanner.nextLine();
            if(inputReader.equals("start")) {
                if (entityCounter == 0) {
                    System.out.println("Expected entity name.");
                }
                break;
            }

            String[] inputList = inputReader.trim().split(" ");
            entityCounter += 1;

            if (inputList[0].equals("OBS")) {
                Obstacle o = obstacleEngine(inputList, entityCounter, width, height);
                if (o != null) obstacles.add(o);

            } else if (inputList[0].equals("ROB")) {
                robotCounter += 1;
                Robot r = roboEngine(inputList, entityCounter, width, height, robotCounter);
                if (r != null) robots.add(r);

            } else if (inputList[0].equals("OBJ")) {
                objectCounter += 1;
                InitObject obj = objectEngine(inputList, entityCounter, width, height, objectCounter);
                if (obj != null) objects.add(obj);

            } else {
                System.out.println("Unknown entity type \"" + inputList[0] + "\" at entity " + entityCounter);
            }
        }

        //verifica se tem pelo menos 1 robô e 1 objeto
        if (robots.isEmpty() || objects.isEmpty()) {
            System.out.println("At least 1 robot and 1 object are required.");
        }
    }

    private Obstacle obstacleEngine(String[] parts, int entityCounter, int width, int height){
        if(parts.length != 3) {
            System.out.println("Invalid entity format at entity " + entityCounter);
            return null;
        }
        try{
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);

            if (!validationHelper(x, y, width, height)) {
                System.out.println("Invalid entity coordinates at entity " + entityCounter);
                return null;
            }
            return new Obstacle(new Position(x, y));
        } catch (NumberFormatException e) {
            System.out.println("Invalid entity coordinates at entity " + entityCounter);
            return null;
        }
    }

    private InitObject objectEngine(String[] parts, int entityCounter, int width, int height, int objectId) {
        if(parts.length != 3) {
            System.out.println("Invalid entity format at entity " + entityCounter);
            return null;
        }
        try{
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);

            if (!validationHelper(x, y, width, height)) {
                System.out.println("Invalid entity coordinates at entity " + entityCounter);
                return null;
            }
            return new InitObject(new Position(x, y), objectId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid entity coordinates at entity " + entityCounter);
            return null;
        }
    }

    private Robot roboEngine(String[] parts, int entityCounter, int width, int height, int robotId) {
        if(parts.length != 5) {
            System.out.println("Invalid entity format at entity " + entityCounter);
            return null;
        }
        try {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int csx = Integer.parseInt(parts[3]);
            int csy = Integer.parseInt(parts[4]);

            if(!validationHelper(x, y, width, height) || !validationHelper(csx, csy, width, height)){
                System.out.println("Invalid entity coordinates at entity " + entityCounter);
                return null;
            }

            ChargingStation cs = new ChargingStation(new Position(csx, csy), robotId);
            return new Robot(new Position(x, y), robotId, cs);

        } catch(NumberFormatException e){
            System.out.println("Invalid entity coordinates at entity " + entityCounter);
            return null;
        }
    }

    //métodos de apoio
    private boolean validationHelper(int x, int y, int width, int height){
        return x >= 1 && y >= 1 && x <= width && y <= height;
    }

    private boolean positionOccupiedHelper(int x, int y, List<Entity> entities){
        for(Entity e : entities){
            if(e.getPosition().getX() == x && e.getPosition().getY() == y){
                return true;
            }
        }
        return false;
    }
}


//TODO: Dimensões — 3 7 dá erro de mínimo
//No teu código tens:
//javaif (width < 5 || height < 5)
//O input 3 7 — largura é 3, menor que 5 → correto, imprime o erro.
//
//Mas há um problema de lógica
//No readDimensions quando as dimensões são inválidas (min ou max) imprimes o erro e fazes return null — mas o programa deve terminar ou continuar a pedir input?
//Olhando para a tabela, parece que imprime o erro e termina — o Mooshak não mostra mais output depois do erro.
//Isso significa que quando readProject recebe null do readDimensions deve terminar o programa com System.exit(0).

//aiai o copypaste !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//foi só no todo ahahaha
