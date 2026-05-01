import java.util.Scanner;
import java.util.List;
// ler os inputs e validar
//Apenas validar inputs de range, se é number, etc
//Não há validações de funcionalidade aqui

public class InputReader {

    private Scanner scanner = new Scanner(System.in);

    //Construtor
    public InputReader(Scanner scanner){
        this.scanner = scanner;
    }

    //métodos principais da classe

    //readProject é o único público porque vai ser chamado de fora
    public Project readProject(){

    }

    private int[] readDimensions(){

    }

    private void readEntities(){

    }

    //métodos de apoio
    private boolean isValidCoordinates(int x, int y, int width, int height){

    }

    private boolean isPositionOccupied(int x, int y, List<Entity> entities){

    }
}
