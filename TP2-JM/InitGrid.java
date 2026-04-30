import java.util.Scanner;
// criação da grelha e instalação
 
public class InitGrid {
 
 
    public void main(String[] args) {
 
        private int gridHeight = 0;
        private int gridWidth = 0;
        private String[][] grid;
 
        Scanner gridValues = new Scanner(System.in);
        String gridRawValues = gridValues.nextLine();
        String[] values = gridRawValues.trim().split(" ");
 
        if (values.length == 2){ //atribuir valores a input e validação para ver se tem 2 valores
           
            //primeiro vamos extrair os valores do nosso array e depois atribuir o valor de inteiro
           
            String rawWidthStr = values[0]; //largura
            gridWidth = Integer.parseInt(rawWidthStr);
 
            String rawHeightStr = values[1]; //altura
            gridHeight = Integer.parseInt(rawHeightStr);
 
        }else {
            System.out.println("Error: Your input must have 2 values");
        }
 
        //validação da posição
        private boolean isAvaliable(int l, int c) {
            return l >= 0 && l < gridHeight && c >= 0 && c < gridWidth;
        }
 
 
        //inicializar objeto da tabela
        public Grid(int width, int height) {
            this.gridWidth = width;
            this.gridHeight = height;
            this.grid = new String[height][width];
            startGrid();
        }
 
        //meter .. em todos os espaços e as numerações
        private void startGrid() {
 
            int y=0;
            int o=0;
 
 
            System.out.print("   ");
            for (int i = 0; i < gridWidth; i++) { //imprimir a primeira linha com a numeração
                o++;
                System.out.printf("%02d",o);
                System.out.print(" ");
            }
            System.out.println();
            for (int l = 0; l < gridHeight; l++) { //imprimir a tabela em si
                y++;
                System.out.printf("%02d", y);
                System.out.print(" ");
                for (int c = 0; c < gridWidth; c++) {
                   
                    System.out.print(grid[l][c] + " ");
                }
                System.out.println();
               
            }
                for (int l = 0; l < gridHeight; l++) { //preencher os espaços todos com ..
                    for (int c = 0; c < gridWidth; c++) {
                        grid[l][c] = "..";
                    }
               
                }
               
        }
 
        public void setGridEntity (int l, int c, String entity){
            if (isAvaliable(l,c) == true) {
                grid[l][c] = entity;
               
            }
        }
 
        public String getGridEntity (int l, int c) {
            return grid[l][c];
        }
 
 
 
 
 