//representar a estação de carregamento do robo
//por iteração + 10%

/**
 * Classe que representa a estação de carregamento associada a um {@link Robot}.
 *
 * <p>Extende {@link Entity} para herdar a posição na grelha. Cada estação está
 * ligada a um robot específico através do seu id, sendo o símbolo na grelha
 * composto por "S" seguido do id do robot.</p>
 *
 * @see Robot
 * @see Entity
 */
public class ChargingStation extends Entity {

    private final int roboId;

    /**
     * Construtor da Classe que cria uma nova estação de carregamento na posição indicada.
     *
     * @param position  posição da estação na grelha
     * @param roboId    id do robot ao qual esta estação pertence
     */
    public ChargingStation(Position position, int roboId) {
        super(position);
        this.roboId = roboId;
    }

    /**
     * Getter de RoboId.
     * @return
     */
    public int getRoboId() {
        return this.roboId;
    }

    /**
     * Override do método getSymbol, devolve o símbolo da estação na grelha.
     * @return
     */
    @Override
    public String getSymbol() {
        return "S" + roboId;
    }
}