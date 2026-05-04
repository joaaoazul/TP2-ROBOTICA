
//representar tarefa criada pelo user 

public class Task {

    private final int objId;
    private Position objectPosition;
    private Position finalDestination;
    private TaskStatus status;

    public Task(int objId, Position objectPosition, Position finalDestination) {
        this.objId = objId;
        this.objectPosition = objectPosition;
        this.finalDestination = finalDestination;
        this.status = TaskStatus.FREE;
    }

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


