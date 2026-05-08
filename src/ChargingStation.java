
/**
 * Classe que representa a Charging station do Robot, onde o mesmo pode carregar as suas energias.
 * Extende as funções da classe pai Entity mas usa um atributo @param roboId para identificar de que Robot é a estação.
 */
public class ChargingStation extends Entity {

    /**@param roboId para identificar a estação e o Robot */
    private final int roboId;

    /**
     * Construtor da classe, recebe a posição da estação através de super Position e o roboId para identificar a estação.
     */
    public ChargingStation(Position position, int roboId){
        super(position);
        this.roboId = roboId; 
    }

    /**
     * Método @param getRoboId para retornar o roboId da estação.
     */

    public int getRoboId () {
        return this.roboId;
    }

    /**
     * Override do método @param getSymbol para devolver o símbolo da estação, neste caso "S", seguido do roboId para identificar qual é o robot que pertence a esta estação.
     */
    
    @Override
    public String getSymbol(){
        return "S" + roboId;
    }

}