//construtor principal da entidade como objeto, obstaculo etc

/**
 * Classe Pai que representa toda e qualquer entidade do nosso projeto, como robôs ou obstáculos, etc.
 * representa qualquer entidade do nosso projeto como robôs ou até obstaculos 
 */

public abstract class Entity {

    /**
     * Declara o atributo @param position que representa a posição de cada entidade no projeto.
     */

    private Position position;

    /**
     * @param position é também a peça central do construtor da classe, sendo que é necessária para criar uma nova entidade.
     */

    public Entity(Position position){
        this.position = position;
    }

    /**
     * Getter @param getPosition e setter @param setPosition para o atributo position, permitindo aceder ou alterar a posição de cada entidade.
     */

    public Position getPosition(){
        return this.position;
    }

    public void setPosition(Position position){
        this.position = position;
    }
    
    /**
     * @param getSymbol , método abstrato que cada classe deve implementar para retornar o símbolo que a representa, como ## para um obstáculo ou O1 para um robô, por exemplo.
     */

    public abstract String getSymbol(); //que nos vai mostrar o symbolo de cada entidade como ## ou O1 por exemplo
}
