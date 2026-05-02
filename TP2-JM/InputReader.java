import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
// Valida formato das entidades e requisitos mínimos
// Validações de coordenadas e dimensões ficam no InitGrid

public class InputReader {

    private Scanner scanner = new Scanner(System.in);
    private List<Obstacle> obstacles = new ArrayList<>();
    private List<Robot> robots = new ArrayList<>();
    private List<InitObject> objects = new ArrayList<>();

    //Construtor
    public InputReader(Scanner scanner){
        this.scanner = scanner;
    }

    //métodos principais da classe

    //readProject é o único público porque vai ser chamado de fora
    public Project readProject(){
        //TODO: FALTA FAZER ESTE
        return null;
    }

    private int[] readDimensions(){
        //recebe os números em string, separa-os e guarda nesse array
        String line = scanner.nextLine();
        String[] input = line.trim().split(" ");
        if(input.length != 2){
            System.out.println("Invalid dimensions format.");
            return null;
        }
        //tenta converter para inteiro e verifica dimensões, se não der, manda erro
        try{
            int width = Integer.parseInt(input[0]);
            int height = Integer.parseInt(input[1]);

            if (width < 5 || height < 5) {
                System.out.println("Minimum dimensions allowed is 5 by 5.");
                return null;
            }
            if (width > 99 || height > 99) {
                System.out.println("Maximum dimensions allowed is 99 by 99.");
                return null;
            }
            return new int[]{width, height};
        } catch (NumberFormatException e) {
            System.out.println("Invalid dimensions format.");
            return null;
        }
    }

    private void readEntities(int width, int height){
        int entityCounter = 0;
        int robotCounter = 0;
        int objectCounter = 0;

        while(true) {
            String start = scanner.nextLine();
            if(start.equals("start")) {
                if (entityCounter == 0) {
                    System.out.println("Expected entity name.");
                }
                break;
            }

            String[] entityInput = start.trim().split(" ");
            entityCounter += 1;

            if (entityInput[0].equals("OBS")) {
                Obstacle o = parseObstacle(entityInput, entityCounter, width, height);
                if (o != null) obstacles.add(o);

            } else if (entityInput[0].equals("ROB")) {
                robotCounter += 1;
                Robot r = parseRobot(entityInput, entityCounter, width, height, robotCounter);
                if (r != null) robots.add(r);

            } else if (entityInput[0].equals("OBJ")) {
                objectCounter += 1;
                InitObject obj = parseObject(entityInput, entityCounter, width, height, objectCounter);
                if (obj != null) objects.add(obj);

            } else {
                System.out.println("Unknown entity type \"" + entityInput[0] + "\" at entity " + entityCounter);
            }
        }

        //verifica se tem pelo menos 1 robô e 1 objeto
        if (robots.isEmpty() || objects.isEmpty()) {
            System.out.println("At least 1 robot and 1 object are required.");
        }
    }

    private Obstacle parseObstacle(String[] parts, int entityCounter, int width, int height){
        if(parts.length != 3) {
            System.out.println("Invalid entity format at entity " + entityCounter);
            return null;
        }
        try{
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);

            if (!isValidCoordinates(x, y, width, height)) {
                System.out.println("Invalid entity coordinates at entity " + entityCounter);
                return null;
            }
            return new Obstacle(new Position(x, y));
        } catch (NumberFormatException e) {
            System.out.println("Invalid entity coordinates at entity " + entityCounter);
            return null;
        }
    }

    private InitObject parseObject(String[] parts, int entityCounter, int width, int height, int objectId) {
        if(parts.length != 3) {
            System.out.println("Invalid entity format at entity " + entityCounter);
            return null;
        }
        try{
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);

            if (!isValidCoordinates(x, y, width, height)) {
                System.out.println("Invalid entity coordinates at entity " + entityCounter);
                return null;
            }
            return new InitObject(new Position(x, y), objectId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid entity coordinates at entity " + entityCounter);
            return null;
        }
    }

    private Robot parseRobot(String[] parts, int entityCounter, int width, int height, int robotId) {
        if(parts.length != 5) {
            System.out.println("Invalid entity format at entity " + entityCounter);
            return null;
        }
        try {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int csx = Integer.parseInt(parts[3]);
            int csy = Integer.parseInt(parts[4]);

            if(!isValidCoordinates(x, y, width, height) || !isValidCoordinates(csx, csy, width, height)){
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
    private boolean isValidCoordinates(int x, int y, int width, int height){
        return x >= 1 && y >= 1 && x <= width && y <= height;
    }

    private boolean isPositionOccupied(int x, int y, List<Entity> entities){
        for(Entity e : entities){
            if(e.getPosition().getX() == x && e.getPosition().getY() == y){
                return true;
            }
        }
        return false;
    }
}