
// criação da grelha e instalação


import java.util.Scanner;

/**
 * Classe responsável por criar a Grid e imprimir no terminal a grelha inicial, bem como validar as posições e permitir a colocação de entidades na grelha.
 */

public class InitGrid {

    /**
     * Atributos da classe:
     * @param gridHeight - altura da grelha
     * @param gridWidth - largura da grelha
     * @param grid - matriz da grelha, onde cada posição pode alojar uma entidade, ou um espaço vazio representado por ".."
     */
    private int gridHeight = 0;
    private int gridWidth = 0;
    private String[][] grid;

    /**
     * Método construtor da classe, recebe a largura e altura da grelha, inicia a grelha e introduz espaços vazios em todas as suas posições.
     * @param width - largura da grelha
     * @param height - altura da grelha
     */

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
    }

    /**
     * Método @param printGrid responsável por imprimir a grelha no terminal, com a númeração e formatação das linhas, colunas e espaços vazios.
     */

    public void printGrid() {
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

    /**
     * Método @param isAvaliable responsável por validar se a posição dada é válida, ou seja, se está dentro dos limites da grelha.
     */
    //validaçao da posição
    private boolean isAvaliable(int l, int c) {
        return l >= 0 && l < gridHeight && c >= 0 && c < gridWidth;
    }

    /**
     * Método @param setGridEntity responsável por colocar uma entidade na grelha, caso a posição seja válida e esteja disponível (ou seja, não tenha outra entidade).
     */

    public void setGridEntity(int l, int c, String entity) {
        if (isAvaliable(l, c) == true) { 
            grid[l][c] = entity;
        }
    }

    /**
     * Método @param getGridEntity responsável por @return a entidade atual na posição dada da grelha, ou um espaço vazio caso não haja nenhuma entidade.
     */

    public String getGridEntity(int l, int c) {
        return grid[l][c];
    }

    /**
     * Método @param main responsável por ler as dimensões da grelha a partir do terminal, validar as dimensões e criar a grelha inicial, imprimindo-a no terminal.
     */

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
                myGrid.printGrid();
            }
            
        } else {
            System.out.println("Invalid dimensions format.");
        }
    }

    /**
     * Métodos @param getWidth e @param getHeight getters responsáveis por devolver a largura e altura da grelha, respetivamente.
     */
    public int getWidth(){
        return this.gridWidth;
    }
    public int getHeight(){
        return this.gridHeight;
    }
}