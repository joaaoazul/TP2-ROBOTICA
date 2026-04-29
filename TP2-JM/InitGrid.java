import java.util.Scanner;
// criação da grelha e instalação

public class InitGrid {

    

    public static void main(String[] args) {
        Scanner gridValues = new Scanner(System.in);
        String gridRawValues = gridValues.nextLine();
        String[] values = gridRawValues.trim().split(" ");

        if (values.length == 2){ //validação para ver se tem 2 valores 
            
            //primeiro vamos extrair os valores do nosso array e depois atribuir o valor de inteiro
            
            String rawWidthStr = values[0]; //largura
            int gridWidth = Integer.parseInt(rawWidthStr);

            String rawHeightStr = values[1]; //altura
            int gridHeight = Integer.parseInt(rawHeightStr);

        }else {
            System.out.println("Error: Your input must have 2 values");
        }

        for (int i = 1; i < gridWidth + 1; i++) {
            System.out.printf("%02d", i); //%02d para ficar 01 e nao 1
        }
        for (int i = 1; i < gridHeight + 1; i++) {
            System.out.printf("%02d", i);
        }


        
    }

   // gridHeight = gridRawValues.charAt(2)


}
