
/**
 * Classe representa o Objeto, recebe as propriedades de objeto da classe Pai Entity, tem um id para identificar o objeto e um boolean para verificar se o objeto esta a ser transportado ou não.
 */

public class InitObject extends Entity {

    /**
     * @param id - id do objeto, para identificar o objeto, imútavel, ou seja, não pode ser alterado depois de criado.
     * @param beingCarried - boolean para verificar se o objeto esta a ser transportado ou não, este sim pode ser alterado durante a execução do programa.
     */

    private final int id; //o id vai ser sempre o mesmo
    private boolean beingCarried = false;

    /**
     * Construtor da classe, recebe a posição e id do objeto e chama o construtor da classe pai, através de super para inciar a posição do objeto, logo de seguida atribui o id.
     */

    public InitObject(Position position, int id){
        super(position);
        this.id = id;
    }

    /**
     * Métodos getter e setter para id e beingCarried, o id tem apenas um getter pois como anteriormente referido, é imútavel, ou seja, não pode ser alterado depois de criado, 
     * já o beingCarried tem um getter e um setter para poder ser alterado durante a execução do programa.
     */

    public int getId(){
        return id;
    }

    public boolean isBeingCarried(){
        return this.beingCarried;
    }

    public void setBeingCarried(boolean beingCarried){
        this.beingCarried = beingCarried;
    }

    /**
     * Método @param getSymbol, retorna o símbolo do objeto, que neste caso é "O" seguido do id do objeto, sendo utilizado para representar o objeto no mapa.
     */

    @Override
    public String getSymbol(){
        return "O" + id;
    }

}
