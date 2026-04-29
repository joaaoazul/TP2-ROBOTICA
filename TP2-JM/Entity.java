//construtor principal da entidade como objeto, obstaculo etc



public abstract class Entity {

    private Position position;

    public Entity(Position position){
        this.position = position;
    }

    public Position getPosition(){
        return this.position;
    }

    public void setPosition(Position position){
        this.position = position;
    }
}
