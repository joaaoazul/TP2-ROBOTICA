
//representar a estação de carregamento do robo
//por iteração + 10%

public class ChargingStation extends Entity {

    private final int roboId;


    public ChargingStation(Position position, int roboId){
        super(position);
        this.roboId = roboId;
    }

    public int getRoboId () {
        return this.roboId;
    }
    
    @Override
    public String getSymbol(){
        return "S" + roboId;
    }

}