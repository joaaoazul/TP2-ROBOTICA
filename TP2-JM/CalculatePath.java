
// implementar a busca em profundidade DFS e clacular rota

import java.util.ArrayList;
import java.util.List;

public class CalculatePath {

    public static boolean canStep(InitGrid grid, int x, int y, List<Position> visited, int targetObjectId ){ //validações todas para ver se pode fazer o passo
        if (x < 1 || x > grid.getWidth() || y < 1 || y > grid.getHeight() ){
            return false;
        }

        String cellContent = grid.getGridEntity(x, y);

        if (cellContent.equals("##")){ //se for obstaculo
            return false;
        } else if (cellContent.startsWith("S") || cellContent.startsWith("C")){   //se for estação de carregamento com robô ou sem
            return false;
        } else if (cellContent.startsWith("O")){
            try {
                int objId = Integer.parseInt(cellContent.substring(1));
                if (objId == targetObjectId){
                    return true;
                }else {
                    return false;
                }
            } catch (Exception e) {
                return false; //se der erro a ler 
            }
        }
        return true;

    }
    
    public static List<Position> searchPath(InitGrid grid, Position start, Position end, int targetObjectId){
        List<Position> path = new ArrayList<>();
        List<Position> visited = new ArrayList<>();

        path.add(start);
        visited.add(start);

        while (!path.isEmpty()){ 
            Position current = path.get(path.size() - 1); //a casa onde tamos é a ultima do path //???
            if(current.getX() == end.getX() && current.getY() == end.getY()){ //se chegar ao destino
                return path;
            }
            int x = current.getX();
            int y = current.getY();

            if (canStep(grid, x - 1, y, visited, targetObjectId)){ //tentar para a esquerda

                Position next = new Position(x-1,y);

                visited.add(next);
                path.add(next);
                continue;//para voltar ao inicio do while

            }else if (canStep(grid, x, y-1, visited, targetObjectId)){ //tentar para cima cima
                Position next = new Position(x, y-1);
                visited.add(next);
                path.add(next);
                continue;

            }else if (canStep(grid, x+1, y, visited, targetObjectId)){ //tentar para a direita
                Position next = new Position(x+1, y);
                visited.add(next);
                path.add(next);
                continue;

            }else if (canStep(grid, x, y+1, visited, targetObjectId)){ //tentar para baixo
                Position next = new Position(x, y+1);
                visited.add(next);
                path.add(next);
                continue;

            }
            path.remove(path.size()-1); // se nao consegue nenhum, recua 1 apagando o ultimo passo da lista 
            
        }
        return null; //caso nao dê nenhum retorna null
    }
}
