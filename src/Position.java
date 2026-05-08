
/**
 * Representa uma posição no tabuleiro, usando as coordenadas (x e y), por meio de váriaveis int.
 * A classe tem um construtor para dar inicio às coordenadas e, usa getters para retornar o valor de cada uma.
 */

public class Position {
    private int x;
    private int y;

    /**
     * O construtor que recebe as coordenadas (x e y) e as atribui às variáveis da classe.
     *
     * @param x coordenada horizontal (coluna)
     * @param y coordenada vertical (linha)
     */

    public Position(int x, int y) {
        this.x = x;
        this.y = y;

    }

    /** 
     * @return o valor de x da coordenada desta posição
     */

    public int getX(){
        return this.x;
    }

     /** 
     * @return o valor de y da coordenada desta posição
     */

    public int getY(){
        return this.y;
    }

/**
 * Duas posições são consideradas iguais, se ambas tiverem as mesmas coordenadas (x e y). 
 * O método equals é overriden para comparar as coordenadas de duas instâncias de Position, 
 * vai comparar @param obj com a instância atual (this) e, @return {@code true} se forem iguais, ou {@code false} caso contrário.
 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.x == other.x && this.y == other.y;
    }

    /**
     * Neste método, o hashCode é overriden também e usamos o 32 * x + y para gerar um hash único que representa a posição e garante que posições iguais tenham o mesmo.
     */

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

}
