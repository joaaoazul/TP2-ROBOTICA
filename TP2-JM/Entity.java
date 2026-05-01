//construtor principal da entidade como objeto, obstaculo etc

/**
 * representa qualquer entidade do nosso projeto como robôs ou até obstaculos 
 */

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

    public abstract String getSymbol(); //que nos vai mostrar o symbolo de cada entidade como ## ou O1 por exemplo
}
