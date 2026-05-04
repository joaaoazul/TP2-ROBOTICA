
//representar objetos trasportaveis

/**
 * Representa um objeto transportável presente na instalação robótica.
 */

public class InitObject extends Entity {

    private final int id; //o id vai ser sempre o mesmo
    private boolean beingCarried = false;

    public InitObject(Position position, int id){
        super(position);
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public boolean isBeingCarried(){
        return this.beingCarried;
    }

    public void setBeingCarried(boolean beingCarried){
        this.beingCarried = beingCarried;
    }

    @Override
    public String getSymbol(){
        return "O" + id;
    }

}
