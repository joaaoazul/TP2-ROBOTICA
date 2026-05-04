
//representar tarefa criada pelo user 

public class Task {
    
private final int objId;

private Position finalDestination;

private TaskStatus status;

public Task(int objid, Position finalDestination){
    this.objId = objId;
    this.finalDestination = finalDestination;
    this.status = TaskStatus.FREE;

}

public int getObjId() {
    return this.objId;

}
