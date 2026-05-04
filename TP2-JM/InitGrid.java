
// criação da grelha e instalação


import java.util.Scanner;

public class InitGrid {
    private int gridHeight = 0;
    private int gridWidth = 0;
    private String[][] grid;

    public InitGrid(int width, int height) {
        this.gridWidth = width;
        this.gridHeight = height;
        this.grid = new String[height][width];
        
        //preencher tudo com espaços vazios
        for (int l = 0; l < gridHeight; l++) { 
            for (int c = 0; c < gridWidth; c++) {
                grid[l][c] = "..";
            }
        }
        
        startGrid();
    }

    private void startGrid() {
        int y = 0;
        int o = 0;


        System.out.print("   ");


        for (int i = 0; i < gridWidth; i++) { //linha da numeração no inicio
            o++;
            System.out.printf("%02d", o);
            System.out.print(" ");
        }

        System.out.println();
        

        for (int l = 0; l < gridHeight; l++) { //tabela em si

            y++;

            System.out.printf("%02d", y); //%02d pra ficar 01 em vez de 1 por ex
            System.out.print(" ");

            for (int c = 0; c < gridWidth; c++) {
                System.out.print(grid[l][c] + " "); 
            }

            System.out.println(); //pra mudar de linha
        }
    }

    //validaçao da posição
    private boolean isAvaliable(int l, int c) {
        return l >= 0 && l < gridHeight && c >= 0 && c < gridWidth;
    }

    public void setGridEntity(int l, int c, String entity) {
        if (isAvaliable(l, c) == true) { 
            grid[l][c] = entity;
        }
    }

    public String getGridEntity(int l, int c) {
        return grid[l][c];
    }

    public static void main(String[] args) {

        Scanner gridValues = new Scanner(System.in);
        String gridRawValues = gridValues.nextLine();
        String[] values = gridRawValues.trim().split(" ");

        if (values.length == 2) { 
            int w = Integer.parseInt(values[0]); // largura
            int h = Integer.parseInt(values[1]); // altura

            // Validações do enunciado para não perdermos pontos
            if (w < 5 || h < 5) {
                System.out.println("Minimum dimensions allowed is 5 by 5.");
            } else if (w > 99 || h > 99) {
                System.out.println("Maximum dimensions allowed is 99 by 99.");
            } else {
                InitGrid myGrid = new InitGrid(w, h);
            }
            
        } else {
            System.out.println("Invalid dimensions format.");
        }
    }
    public int getWidth(){
        return this.gridWidth;
    }
    public int getHeight(){
        return this.gridHeight;
    }
}