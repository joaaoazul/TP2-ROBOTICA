
// representar o comportamento autonomo do robo

import java.util.List;

public class Robot extends Entity {

    private int id;

    private double charge = 100.0;

    private RobotState state = RobotState.IDLE;

    private RobotPhase phase = RobotPhase.NONE;

    private ChargingStation chargingStation;

    private InitObject carriedObject = null;

    private Task currentTask = null;

    private List<Position> currentPath;

    private int currentPathIndex;


    public Robot(Position position, int id, ChargingStation chargingStation) {
        super(position);
        this.id = id;
        this.chargingStation = chargingStation;
    }

}


