
// representar o comportamento autonomo do robo

import java.util.List;

public class Robot extends Entity {

    private int id;
    private double charge = 100.0;
    private RobotState state = RobotState.IDLE;
    private RobotPhase phase = RobotPhase.NONE;
    private ChargingStation chargingStation;
    private InitObject caughtObject = null;
    private Task currentTask = null;
    private List<Position> currentPath;
    private int currentPathIndex;


    public Robot(Position position, int id, ChargingStation chargingStation) {
        super(position);
        this.id = id;
        this.chargingStation = chargingStation;
        this.charge = 100.0;//pois os robôs começam a 100%
    }

    //id
    public int getId() {
        return this.id; 
    }

    //charge
    public double getCharge() {
        return this.charge;
    }
    public void setCharge(double charge) {
        if (charge < 100.0 && charge > 0.0 ){
            this.charge = charge;
        }
        
    }

    //phase
    public RobotPhase getPhase() {
        return this.phase; 
    }
    public void setPhase(RobotPhase phase){
        this.phase = phase;
    }

    //state
    public RobotState getState() {
        return this.state;
    }

    public void setState(RobotState state){
        this.state = state;
    }

    //charging station
    public ChargingStation getChargingStation() {
        return this.chargingStation;
    }

    //caught object
    public InitObject getCaughtObject(){
        return this.caughtObject;
    }

    public void setCaughtObject(InitObject caughtObject){
        this.caughtObject = caughtObject;
    }

    //task
    public Task getCurrentTask(){
        return this.currentTask;
    }
    public void setCurrentTask(Task currentTask){
        this.currentTask = currentTask;
    }

    //current path
    public List<Position> getCurrentPath(){
        return this.currentPath;
    }
    public void setCurrentPath(List<Position> currentPath){
        this.currentPath = currentPath;
    }

    //current path index
    public int getCurrentPathIndex(){
        return this.currentPathIndex;
    }
    public void setCurrentPathIndex(int currentPathIndex){
        this.currentPathIndex = currentPathIndex;

    }



    public void executeStep(Project project){
        switch (this.state) {

            case IDLE: //esta parado fora da estacao so perde 0,2%
                if (this.getPosition() != this.chargingStation.getPosition()) {
                    this.charge -= 0.2;
                }
                break;

            case BUSY: // cada iteração perde 0,5% mas vai ser tirado no movePath
                movePath();

                break;

            case CHARGING:
                this.charge = this.charge + 10.0;
                if (this.charge >=100) { // se carregou fica a 100 e pronto para uma nova task
                    this.charge = 100.0;
                    this.state = RobotState.IDLE;
                    
                }
                break;
        }    
    }

    private void movePath(){
        if (currentPathIndex = currentPath.size() || currentPath == null){ //resolver erro de formato!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            return;
        }
        this.charge = this.charge - 0.5;

        Position nextStep = currentPath.get(currentPathIndex);
        this.setPosition(nextStep);
        currentPathIndex++; //para olhar para a proxima posição

        if (currentPathIndex == currentPath.size()){ //se chegou ao fim do caminho ou apanha o objeto ou larga
            endPath();
        }
    }

    private void endPath(){
        switch (this.phase) {
            case :
                
                break;
            default:
                throw new AssertionError();
        }
    }
    
    
}

// falta o pickup obj
//         drop obj
//         tryTask
//         canCompleteTask