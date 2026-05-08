//representar tarefa criada pelo user

/**
 * Classe que representa uma tarefa criada pelo User para transporte de um objeto.
 *
 * <p>Cada tarefa define o objeto a transportar, a sua posição inicial na grelha
 * e o destino final. O seu ciclo de vida é controlado pelo {@link TaskStatus}.</p>
 *
 * <ul>
 *   <li>{@link TaskStatus#FREE} - disponível para ser atribuída a um robot</li>
 *   <li>{@link TaskStatus#ASSIGNED} - atribuída a um robot em execução</li>
 *   <li>{@link TaskStatus#COMPLETED} - concluída com sucesso</li>
 * </ul>
 *
 * @see Robot
 * @see TaskStatus
 */
public class Task {

    private final int objId;
    private Position objectPosition;
    private Position finalDestination;
    private TaskStatus status;

    /**
     * Construtor da Classe que cria uma nova tarefa com estado inicial {@link TaskStatus#FREE}.
     *
     * @param objId             id do objeto a transportar
     * @param objectPosition    posição atual do objeto na grelha
     * @param finalDestination  posição de destino do objeto
     */
    public Task(int objId, Position objectPosition, Position finalDestination) {
        this.objId = objId;
        this.objectPosition = objectPosition;
        this.finalDestination = finalDestination;
        this.status = TaskStatus.FREE;
    }

    /**
     * Getters de ObjectId, ObjectPosition, End e Status, e Setter de Status, respetivamente.
     * @return
     */

    public int getObjectId() {
        return this.objId;
    }

    public Position getObjectPosition() {
        return this.objectPosition;
    }

    public Position getEnd() {
        return this.finalDestination;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}