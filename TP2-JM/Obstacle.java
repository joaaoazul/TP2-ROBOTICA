// representar obstaculos 

public class Obstacle extends Entity {

   public Obstacle(Position position){
       super(position);
   }

   //denominar o simbolo do obstaculo
   @Override
   public String getSymbol() {
    return "##";
   }

}