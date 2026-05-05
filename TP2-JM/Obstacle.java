// representar obstaculos 
/**
 * Classe Obstacle representa um obstáculo na grid. Ela herda da classe Pai Entity a sua @param position e define o símbolo específico para representar o obstáculo no mapa.
 */
public class Obstacle extends Entity {

/**
 * Construtor da classe Obstacle, que por sua vez chama o construtor Pai da Ent
 */

   public Obstacle(Position position){
       super(position);
   }

   //denominar o simbolo do obstaculo
   @Override
   public String getSymbol() {
    return "##";
   }

}