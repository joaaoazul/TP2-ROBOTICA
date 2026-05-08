import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



/**
 * Classe responsável pela leitura e validação do input inicial do sistema.
 *
 * <p>Lê as dimensões da grelha, as entidades (obstáculos, robots e objetos) e
 * constrói o {@link Project} pronto a iniciar. Valida o formato das entidades
 * e requisitos mínimos — validações de coordenadas e dimensões ficam no {@link InitGrid}.</p>
 *
 * <p>Entidades suportadas no input:</p>
 * <ul>
 *   <li>{@code OBS x y} - obstáculo na posição indicada</li>
 *   <li>{@code ROB x y csx csy} - robot na posição indicada com estação de carregamento</li>
 *   <li>{@code OBJ x y} - objeto na posição indicada</li>
 * </ul>
 *
 * @see Project
 * @see InitGrid
 * @see Robot
 */




public class InputReader {

    private Scanner scanner = new Scanner(System.in);
    private List<Obstacle> obstacles = new ArrayList<>();
    private List<Robot> robots = new ArrayList<>();
    private List<InitObject> objects = new ArrayList<>();

    /**
     * Construtor da Classe que inicializa o InputReader com o Scanner fornecido.
     *
     * @param scanner   Scanner para leitura do input
     */



    public InputReader(Scanner scanner){ 
        this.scanner = scanner;
    }

    /**
     * Método principal da classe, que faz toda a leitura completa do input e constrói o {@link Project}.
     * Lê dimensões, entidades e popula a grelha, terminando com {@code System.exit(0)} se alguma
     * validação falhar.
     *
     * @return  projeto inicializado com a grelha e lista de robots
     */


    // é o único público porque vai ser chamado de fora
    public Project projectsEngine(){

        
        int[] dimensions = dimensionsEngine();
        if (dimensions == null) {
            System.exit(0);
        }
      
      int width = dimensions[0];
      int height = dimensions[1];
      
        InitGrid initGrid = new InitGrid(width, height);

      entityEngine(width, height);

         if (robots.isEmpty() || objects.isEmpty()) {
        System.exit(0);
    }

        for (int i = 0; i < obstacles.size(); i++) {
    initGrid.setGridEntity(obstacles.get(i).getPosition().getY() - 1, obstacles.get(i).getPosition().getX() - 1, "##");
}

for (int i = 0; i < robots.size(); i++) {
    initGrid.setGridEntity(robots.get(i).getPosition().getY() - 1, robots.get(i).getPosition().getX() - 1, "R" + robots.get(i).getId());
    initGrid.setGridEntity(robots.get(i).getChargingStation().getPosition().getY() - 1, robots.get(i).getChargingStation().getPosition().getX() - 1, "S" + robots.get(i).getId());
}
for (int i = 0; i < objects.size(); i++) {
    initGrid.setGridEntity(objects.get(i).getPosition().getY() - 1, objects.get(i).getPosition().getX() - 1, "O" + objects.get(i).getId());
} 

    return new Project(initGrid, robots);

    }

    /**
     * Método dimensionsEngine, que lê e valida as dimensões da grelha a partir do input.
     * As dimensões têm que estar entre 5x5 e 99x99, inclusive.
     *
     * @return  array {@code int[]{width, height}} ou {@code null} se inválido
     */

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

            if (width <= 4 || height <= 4) {
                System.out.println("Minimum dimensions allowed is 5 by 5.");
                return null;
            }
            if (width >= 100 || height >= 100) {
                System.out.println("Maximum dimensions allowed is 99 by 99.");
                return null;
            }
            return new int[]{width, height};
        } catch (NumberFormatException e) {
            System.out.println("Invalid dimensions.");
            return null;
        }
    }
    /**
     * Método entityEngine, que lê as entidades linha a linha até encontrar o comando {@code start},
     * criando obstáculos, robots e objetos conforme o tipo indicado.
     * Termina com {@code System.exit(0)} se encontrar entidade desconhecida ou lista inválida.
     *
     * @param width     largura da grelha, para validação de coordenadas
     * @param height    altura da grelha, para validação de coordenadas
     */
    private void entityEngine(int width, int height){
        int entityCounter = 0;
        int robotCounter = 0;
        int objectCounter = 0;

        while(true) {
            String inputReader = scanner.nextLine();
            if(inputReader.equals("start")) {
                if (entityCounter == 0) {
                    System.out.println("Expected entity name.");
                    System.exit(0);
                }
                break;
            }

            String[] inputList = inputReader.trim().split(" ");
            entityCounter += 1;

            if (inputList[0].equals("OBS")) {
                Obstacle o = obstacleEngine(inputList, entityCounter, width, height);
                if (o != null) obstacles.add(o);
                else System.exit(0);

            } else if (inputList[0].equals("ROB")) {
                robotCounter += 1;
                Robot r = roboEngine(inputList, entityCounter, width, height, robotCounter);
                if (r != null) robots.add(r);
                else System.exit(0);

            } else if (inputList[0].equals("OBJ")) {
                objectCounter += 1;
                InitObject obj = objectEngine(inputList, entityCounter, width, height, objectCounter);
                if (obj != null) objects.add(obj);
                else System.exit(0);

            } else {
                System.out.println("Unknown entity type \"" + inputList[0] + "\" at entity " + entityCounter + ".");
                System.exit(0);
            }
        }

        //verifica se tem pelo menos 1 robô e 1 objeto
        if (robots.isEmpty() || objects.isEmpty()) {
            System.out.println("At least 1 robot and 1 object are required.");
            System.exit(0);
        }
    }

    /**
     * Método obstacleEngine, que valida e cria um {@link Obstacle} a partir das partes do input.
     *
     * @param parts         array com o tipo e coordenadas ({@code OBS x y})
     * @param entityCounter índice da entidade atual, usado nas mensagens de erro
     * @param width         largura da grelha
     * @param height        altura da grelha
     * @return              obstáculo criado, ou {@code null} se inválido
     */


    private Obstacle obstacleEngine(String[] parts, int entityCounter, int width, int height){
        if(parts.length != 3) {
            System.out.println("Invalid entity format at entity " + entityCounter + ".");
            return null;
        }
        try{
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);

            if (!validationHelper(x, y, width, height)) {
                System.out.println("Invalid entity coordinates at entity " + entityCounter + ".");
                return null;
            }
            if (positionOccupiedHelper(x, y, allEntitiesHelper())) {
                System.out.println("Invalid entity coordinates at entity " + entityCounter + ".");
                return null;
            }
            return new Obstacle(new Position(x, y));
        } catch (NumberFormatException e) {
            System.out.println("Invalid entity coordinates at entity " + entityCounter + ".");
            return null;
        }
    }

    /**
     * Método objectEngine, que valida e cria um {@link InitObject} a partir das partes do input.
     *
     * @param parts         array com o tipo e coordenadas ({@code OBJ x y})
     * @param entityCounter índice da entidade atual, usado nas mensagens de erro
     * @param width         largura da grelha
     * @param height        altura da grelha
     * @param objectId      id sequencial atribuído ao objeto
     * @return              objeto criado, ou {@code null} se inválido
     */

    private InitObject objectEngine(String[] parts, int entityCounter, int width, int height, int objectId) {
        if(parts.length != 3) {
            System.out.println("Invalid entity format at entity " + entityCounter + ".");
            return null;
        }
        try{
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);

            if (!validationHelper(x, y, width, height)) {
                System.out.println("Invalid entity coordinates at entity " + entityCounter + ".");
                return null;
            }
            if (positionOccupiedHelper(x, y, allEntitiesHelper())) {
                System.out.println("Invalid entity coordinates at entity " + entityCounter + ".");
                return null;
            }
            return new InitObject(new Position(x, y), objectId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid entity coordinates at entity " + entityCounter + ".");
            return null;
        }
    }

    /**
     * Método roboEngine, que valida e cria um {@link Robot} com a respetiva {@link ChargingStation}
     * a partir das partes do input.
     *
     * @param parts         array com o tipo, coordenadas do robot e da estação ({@code ROB x y csx csy})
     * @param entityCounter índice da entidade atual, usado nas mensagens de erro
     * @param width         largura da grelha
     * @param height        altura da grelha
     * @param robotId       id sequencial atribuído ao robot
     * @return              robot criado, ou {@code null} se inválido
     */

    private Robot roboEngine(String[] parts, int entityCounter, int width, int height, int robotId) {
        if(parts.length != 5) {
            System.out.println("Invalid entity format at entity " + entityCounter + ".");
            return null;
        }
        try {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int csx = Integer.parseInt(parts[3]);
            int csy = Integer.parseInt(parts[4]);

            if(!validationHelper(x, y, width, height) || !validationHelper(csx, csy, width, height)){
                System.out.println("Invalid entity coordinates at entity " + entityCounter + ".");
                return null;
            }
            List<Entity> all = allEntitiesHelper();
            if (positionOccupiedHelper(x, y, all) || positionOccupiedHelper(csx, csy, all) || (x == csx && y == csy)) {
                System.out.println("Invalid entity coordinates at entity " + entityCounter + ".");
                return null;
            }

            ChargingStation cs = new ChargingStation(new Position(csx, csy), robotId);
            return new Robot(new Position(x, y), robotId, cs);

        } catch(NumberFormatException e){
            System.out.println("Invalid entity coordinates at entity " + entityCounter + ".");
            return null;
        }
    }

    //metodos de apoio
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

    private List<Entity> allEntitiesHelper(){
        List<Entity> all = new ArrayList<>();
        all.addAll(obstacles);
        all.addAll(objects);
        for (int i = 0; i < robots.size(); i++) {
            all.add(robots.get(i));
            all.add(robots.get(i).getChargingStation());
        }
        return all;
    }
}

