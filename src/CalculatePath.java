
// implementar a busca em profundidade DFS e clacular rota

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Helper/Utilitária que implementa o mecanismo de busca em profundidade (DFS - Depth-First Search)
 * para calcular rotas numa grelha de simulação.
 * <p>
 * A navegação analisa obstáculos, estações de carregamento e objetos presentes
 * na grelha, permitindo ao robot alcançar um destino ou recolher um objeto.
 * </p>
 */

public class CalculatePath {

      /**
       * Vai calcular e verificar se o robot pode mover-se para a célula nas coordenadas específicadas.
     * <p>
     * Uma célula é considerada válida e aberta se:
     * <ul>
     *   <li>Estiver dentro dos limites da grelha;</li>
     *   <li>Não for um obstáculo ({@code ##});</li>
     *   <li>Não for uma estação de carregamento ({@code S} ou {@code C});</li>
     *   <li>Não tiver sido visitada anteriormente;</li>
     *   <li>Caso contenha um objeto ({@code O}), o seu Id corresponder ao {@code targetObjectId}.</li>
     * </ul>
     * </p>
     *
     * @param grid           grelha de simualção do robot
     * @param x              coordenada X da célula destino (base 1)
     * @param y              coordenada Y da célula destino (base 1)
     * @param visited        posições já visitadas durante a busca, para evitar recursões infinitas
     * @param targetObjectId Id do objeto que o robô pretende recolher;
     *                       usado para permitir passagem por células com esse objeto, devolve
     * @return {@code true} se o movimento for válido; {@code false} caso não seja.
     */

    public static boolean canStep(InitGrid grid, int x, int y, List<Position> visited, int targetObjectId ){ //validações todas para ver se pode fazer o passo
        int width = grid.getWidth();
        int height = grid.getHeight();

        if (x < 1 || x > width || y < 1 || y > height ){
            return false;
        }

        for (int i = 0; i < visited.size(); i++) { //se ja foi visitada, nao pode ir
            Position v = visited.get(i);
            if (v.getX() == x && v.getY() == y) {
                return false;
            }
        }

        String cellContent = grid.getGridEntity(y - 1, x - 1);

        if (cellContent.equals("##")){ //se for obstaculo
            return false;
        } else if (cellContent.startsWith("S") || cellContent.startsWith("C")){   //se for estação de carregamento com robô ou sem
            if (targetObjectId != -1) { //quando o destino é a estação (-1) permite o passo
                return false;
            }
        } else if (cellContent.startsWith("O")){
            try {
                int objId = Integer.parseInt(cellContent.substring(1));
                return objId == targetObjectId;
            } catch (NumberFormatException e) {
                return false; //se der erro a ler
            }
        }
        return true;

    }

     /**
    * Calcula um caminho/rota possível desde o ponto de partida até ao destino usando o mecanismo DFS (Depth-First Search) busca em profundidade.
     * <p>
     * A busca segue uma ordem definida de: esquerda → cima → direita → baixo.
     * Caso nenhuma diração seja viável a partir da posição corrente, o mecanismo retrocede removendo a última posição do caminho.
     * O robot pode atravessar células de objetos com Id como {@code targetObjectId},
     * mas não pode atravessar obstáculos nem estações de carregamento.
     * </p>
     *
     * @param grid           inicia a grelha da simualação
     * @param start          será a posição inicial do robot
     * @param end            destino do robot
     * @param targetObjectId Id do objeto alvo, as células desse objeto são dadas como transitáveis
     * @return uma {@link List} de {@link Position} representra o caminho do início ao fim
     *         (inclusivo de início e fim), ou {@code null} se não houver uma rota possivel/válida
     */
    
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
