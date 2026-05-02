import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
// Valida formato das entidades e requisitos mínimos
// Validações de coordenadas e dimensões ficam no InitGrid

//TODO: Lógica dos métodos
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

    }

    private int[] readDimensions(){
        //recebe o números em string, separa-os e guarda nesse array
        String line = scanner.nextLine();
        String[] input = line.trim().split(" ");
        if(input.length != 2){
            System.out.println("Invalid dimensions format.");
        }

        //tenta converter para inteiro e verifica dimensões, se não der, manda erro
        try{
            int width = Integer.parseInt(input[0]);
            int height = Integer.parseInt(input[1]);

            if (width < 5 || height < 5) {
                System.out.println("Minimum dimensions allowed is 5 by 5.");
            }
            if (width > 99 || height > 99) {
                System.out.println("Maximum dimensions allowed is 99 by 99.");
            }

            return new int[]{width, height};
        } catch (NumberFormatException e) {
            System.out.println("Invalid dimensions format.");
            return null;
        }


    }

    private void readEntities(int width, int height){

        while(true) {
            String start = scanner.nextLine();
            if(start.equals("start")) {
                if (entityCounter == 0) {
                    System.out.println("Expected entity name.");
                }
                break;
            }

            String[] entityInput = start.trim().split(" ");


            if(entityInput[0].equals("OBS")){
                if (entityInput.length != 3){
                    System.out.println("Invalid entity format at entity " + entityCounter);
                    continue;

                }
                try{
                    int x = Integer.parseInt(entityInput[1]);
                    int y = Integer.parseInt(entityInput[2]);

                    if (!isValidCoordinates(x, y, width, height)) {
                        System.out.println("Invalid entity coordinates at entity " + entityCounter);
                        continue;
                    }

                    obstacles.add(new Obstacle(new Position(x, y)));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid entity coordinates at entity " + entityCounter);
                }

            }


        }




        // TODO: parseObstacle - verificar 3 partes, parseInt x e y, isValidCoordinates, return new Obstacle ou null
// TODO: parseObject - igual ao parseObstacle mas com id de objeto, return new InitObject ou null
// TODO: parseRobot - verificar 5 partes, parseInt x y cs-x cs-y, isValidCoordinates para robô e estação, criar ChargingStation, return new Robot ou null
// TODO: isValidCoordinates - verificar se x >= 1, y >= 1, x <= width, y <= height
// TODO: isPositionOccupied - percorrer lista de entidades e ver se alguma tem a mesma posição
// TODO: readProject - chamar readDimensions, chamar readEntities, criar e devolver Project
// TODO: readEntities - adicionar else if para OBJ e ROB, adicionar else para tipo desconhecido, verificar no fim se tem pelo menos 1 robot e 1 objeto

    }

    private Obstacle parseObstacle(String[] parts, int entityCounter, int width, int height){

                if(parts.length != 3) { System.out.println("");}
                try{
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);

                    if (!isValidCoordinates(x, y, width, height)) {
                        System.out.println("Invalid entity coordinates at entity " + entityCounter);
                        return null;
                    }

                    return new Obstacle(Position position);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid entity coordinates at entity " + entityCounter);
                    return null;
                }

            }


        }

    }

    //métodos de apoio
    private boolean isValidCoordinates(int x, int y, int width, int height){

    }

    private boolean isPositionOccupied(int x, int y, List<Entity> entities){

    }
}
