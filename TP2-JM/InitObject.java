
//representar objetos trasportaveis 

public class InitObject extends Entity {

    private int id;
    private boolean beingCarried = false;

    public InitObject(Position position, int id){
        super(position);
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public boolean isBeingCarried(){
        return this.beingCarried;
    }

    public void setBeingCarried(boolean beingCarried){
        this.beingCarried = beingCarried;
    }



}
