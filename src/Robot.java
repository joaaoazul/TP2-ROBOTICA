//TODO: FALTA SÓ JAVADOCS
// representar o comportamento autonomo do robo

import java.util.List;

/**
 * Classe que representa a entidade do Robot, extende a classe Entity para receber atributos Pai,
 * E opera a entidade dentro dos limites da Grid com funções limitadas ao seu tipo.
 *
 * <p>Navega a grid como dito, e pega em objetos, entrega em certos destinos definidos pelo user. O seu comportamento é
 * definido pelo {@link RobotState} (IDLE, BUSY, CHARGING)e o {@link RobotPhase} que orienta o estado atual da entrega de um objeto.</p>
 *
 * <p>O Robot em cada passo simulado consome bateria nos seguintes moldes:
 * </p>
 * <ul>
 *   <li>IDLE fora da estação de carregamento: -0.2% por passo</li>
 *   <li>BUSY (moving): -0.5% por passo</li>
 *   <li>CHARGING: +10.0% por passo, limitado a um máximo de 100%</li>
 *   </ul>
 *  @see RobotState
 *  @see RobotPhase
 *  @see Task
 *  @see ChargingStation
 */

public class Robot extends Entity {

    private int id;
    private double charge = 100.0;
    private RobotState state = RobotState.IDLE;
    private RobotPhase phase = RobotPhase.NONE;
    private ChargingStation chargingStation;
    private InitObject caughtObject = null;
    private Task currentTask = null;
    private List<Position> currentPath;
    private int currentPathIndex;

    /**
     * Método que recebe o simbolo da entidade Robot, neste caso é "R".
     * @return
     */

    @Override
    public String getSymbol() {
        return "R";
    }

    /**
     * Construtor da Classe que vai criar um novo Robot, numa nova posição.
     * @param position      Posição inicial do Robot
     * @param id            Id de identificação único do Robot
     * @param chargingStation       Estação de carregamento associada a esse Robot específico, usada em caso de Idle ou Low Charge.
     */


    public Robot(Position position, int id, ChargingStation chargingStation) {
        super(position);
        this.id = id;
        this.chargingStation = chargingStation;
        this.charge = 100.0;//pois os robôs começam a 100%
    }

    /**
     * Getters de Id, Charge e um Setter de Charge para valores entre 0.0 e 100.0.
     * @return
     */

    //id
    public int getId() {
        return this.id;
    }

    //charge
    public double getCharge() {
        return this.charge;
    }

    public void setCharge(double charge) {
        if (charge <= 100.0 && charge >= 0.0) {
            this.charge = charge;
        }

    }

    /**
     * Getters e Setters de Phase, State, Charging Station, Caught Object, Task, Current Path e Path Index, respectivamente.
     * @return
     */

    //phase
    public RobotPhase getPhase() {
        return this.phase;
    }

    public void setPhase(RobotPhase phase) {
        this.phase = phase;
    }

    //state
    public RobotState getState() {
        return this.state;
    }

    public void setState(RobotState state) {
        this.state = state;
    }

    //charging station
    public ChargingStation getChargingStation() {
        return this.chargingStation;
    }

    //caught object
    public InitObject getCaughtObject() {
        return this.caughtObject;
    }

    public void setCaughtObject(InitObject caughtObject) {
        this.caughtObject = caughtObject;
    }

    //task
    public Task getCurrentTask() {
        return this.currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    //current path
    public List<Position> getCurrentPath() {
        return this.currentPath;
    }

    public void setCurrentPath(List<Position> currentPath) {
        this.currentPath = currentPath;
    }

    //current path index
    public int getCurrentPathIndex() {
        return this.currentPathIndex;
    }

    public void setCurrentPathIndex(int currentPathIndex) {
        this.currentPathIndex = currentPathIndex;

    }

    /**
     * Método executeStop, que executa cada passo do Robot, conforme o seu atual estado.
     * <ul>
     *     <li>{@link RobotState#IDLE} - Se estiver fora da estação, consome 0.2% de carga</li>
     *     <li>{@link RobotState#BUSY} -Avança um passo no atual caminho através de {@link #movePath}</li>
     *      <li>{@link RobotState#CHARGING} - recupera 10.0% de carga e passa a IDLE quando cheia.</li>
     *  </ul>
     * @param project       Contexto atual do projeto, que passa pela verificação lógica de Path e Task
     */

    public void executeStep(Project project) {
        switch (this.state) {

            case IDLE: //esta parado fora da estacao so perde 0,2%
                if (this.getPosition().equals(this.chargingStation.getPosition())) {

                } else {
                    this.charge -= 0.2;
                }
                //tenta apanhar uma tarefa disponivel (sem ir para a estacao de carregamento)
                List<Task> availableTasks = project.getTasks();
                for (int i = 0; i < availableTasks.size(); i++) {
                    Task t = availableTasks.get(i);
                    if (t.getStatus() == TaskStatus.FREE) {
                        if (canCompleteTask(t, project)) {
                            this.currentTask = t;
                            t.setStatus(TaskStatus.ASSIGNED);
                            this.state = RobotState.BUSY;
                            this.phase = RobotPhase.GOING_TO_OBJECT;
                            this.currentPath = CalculatePath.searchPath(project.getInitGrid(), this.getPosition(), t.getObjectPosition(), t.getObjectId());
                            this.currentPathIndex = 1;
                            break;
                        }
                    }
                }
                break;

            case BUSY: // cada iteração perde 0,5% mas vai ser tirado no movePath
                movePath(project);

                break;

            case CHARGING:
                this.charge = this.charge + 10.0;
                if (this.charge >= 100) { // se carregou fica a 100 e pronto para uma nova task
                    this.charge = 100.0;
                    this.state = RobotState.IDLE;

                }
                break;
        }
    }

    /**
     * Método movePath, avança o robo uma posição no percurso já calculado.
     * Consome 0.5% de carga por passo e, quando chega ao destino, delega em {@link #endPath}
     * a competência de mudar de estado/phase.
     *
     * @param project       Contexto do projeto atual.
     */

    private void movePath(Project project) {
        if (currentPath == null || currentPathIndex >= currentPath.size()) {
            return;
        }
        this.charge = this.charge - 0.5;

        Position nextStep = currentPath.get(currentPathIndex);
        this.setPosition(nextStep);
        currentPathIndex++; //para olhar para a proxima posição

        if (currentPathIndex == currentPath.size()) { //se chegou ao fim do caminho ou apanha o objeto ou larga
            endPath(project);
        }
    }

    /**
     * Método endPath, que pega na parte final do percurso do Robot e executa consoante o seu estado/phase.
     *
     *  <ul>
     *     <li>{@link RobotPhase#GOING_TO_OBJECT} - Pega no objeto</li>
     *     <li>{@link RobotPhase#GOING_TO_DESTINATION} - Larga e deixa o objeto</li>
     *     <li>{@link RobotPhase#GOING_TO_CHARGING_STATION} - Faz a transição para o estado de carregamento</li>
     *  </ul>
     *
     * @param project       Contexto do projeto atual.
     */

    private void endPath(Project project) {
        switch (this.phase) {
            case GOING_TO_OBJECT:
                grabObject(project);
                break;

            case GOING_TO_DESTINATION:
                dropObject(project);
                break;

            case GOING_TO_CHARGING_STATION:
                this.state = RobotState.CHARGING;
                this.phase = RobotPhase.NONE;
                break;

            default:
                break;
        }
    }

    /**
     * Método grabObject, que pega no objeto alvo na posição atual, e calcula o percurso até ao destino final.
     * Seguidamente, faz a transição para {@link RobotPhase#GOING_TO_DESTINATION}.
     * @param project       Contexto do projeto atual, usado para calcular o percurso.
     */

    private void grabObject(Project project) {
        this.caughtObject = new InitObject(this.getPosition(), this.currentTask.getObjectId());
        this.phase = RobotPhase.GOING_TO_DESTINATION;
        this.currentPath = CalculatePath.searchPath(project.getInitGrid(), this.getPosition(), this.currentTask.getEnd(), this.currentTask.getObjectId()); //do sitio atual para o destino
        this.currentPathIndex = 1; //como o indice atual é 0 vamos começar no indice 1

    }

    /**
     * Método dropObject, que larga o objeto no destino, marca a Task como {@link TaskStatus#COMPLETED},
     * e logo a seguir tenta pegar noutra tarefa através de {@link #tryPickupTask}.
     * @param project       Contexto do projeto atual.
     */

    private void dropObject(Project project) {
        //atualiza a grelha: o objeto deixa a posicao original e fica no destino
        Position origPos = this.currentTask.getObjectPosition();
        Position destPos = this.currentTask.getEnd();
        int objId = this.currentTask.getObjectId();
        project.getInitGrid().setGridEntity(origPos.getY() - 1, origPos.getX() - 1, "..");
        project.getInitGrid().setGridEntity(destPos.getY() - 1, destPos.getX() - 1, "O" + objId);

        this.caughtObject = null;
        this.currentTask.setStatus(TaskStatus.COMPLETED);
        this.currentTask = null;
        this.state = RobotState.IDLE;
        this.phase = RobotPhase.NONE;

        tryPickupTask(project); //na mesma iteração o robo tem que ver se há mais tarefas
    }

    /**
     * Método boolean canCompleteTask, que verifica que o Robot tem carga suficiente para fazera Task e,
     * voltar à Charging Station logo a seguir.
     *
     * <p>Calcula três segmentos para o percurso:</p>
     *      <ol>
     *          <li>Atual position → object position</li>
     *          <li>Object position → Task destino</li>
     *          <li>Task destino → charging station</li>
     *          </ol>
     *          <p>O custo total é mostrado por {@code (totalSteps) * 0.5}. Retorna {@code false}
     *          se nenhum segmento tiver um percurso válido.</p>
     *
     * @param task      Tarefa a avaliar
     * @param project   Contexto do atual projeto.
     * @return {@code true} se o Robot conseguir cumprir a tarefa e voltar à Charging Station.
     */

    //TODO: Método feito que estava em falta
        private boolean canCompleteTask(Task task, Project project) {
            List<Position> way1 = CalculatePath.searchPath(project.getInitGrid(), this.getPosition(), task.getObjectPosition(), task.getObjectId());
            List<Position> way2 = CalculatePath.searchPath(project.getInitGrid(), task.getObjectPosition(), task.getEnd(), task.getObjectId());
            List<Position> way3 = CalculatePath.searchPath(project.getInitGrid(), task.getEnd(), this.chargingStation.getPosition(), -1);

            if (way1 == null) {
                return false;
            }
            if (way2 == null) {
                return false;
            }
            if (way3 == null) {
                return false;
            }

            double tripCost = (way1.size() - 1 + way2.size() - 1 + way3.size() - 1) * 0.5;

            return this.charge > tripCost;
        }

    /**
     * Método tryPickupTask, vai verificar a lista de Tasks livres que o nosso Robot possa completar e atribui-lhe uma.
     *Se nenhuma Task estiver disponível e o Robot ainda não estiver na sua Charging Station,
     * fá-lo voltar para que se possa carregar.
     *
     * <p>Distribuidor de Tasks mete o estado do Robot {@link RobotState#BUSY} e a sua Phase
     *  em {@link RobotPhase#GOING_TO_OBJECT}, calculando de seguida o Path.</p>
     *
     * @param project       Contexto do atual projeto, contendo a lista de Tasks e a Grid.
     */

    public void tryPickupTask(Project project) { // ter em atenção para fazer a classe TASK
        boolean gotTask = false;

        List<Task> tasks = project.getTasks();

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            if (task.getStatus() == TaskStatus.FREE) {
                if (canCompleteTask(task, project)) {
                    this.currentTask = task;
                    task.setStatus(TaskStatus.ASSIGNED); //marca logo a tarefa como dele

                    this.state = RobotState.BUSY;
                    this.phase = RobotPhase.GOING_TO_OBJECT;

                    this.currentPath = CalculatePath.searchPath(project.getInitGrid(), this.getPosition(), task.getObjectPosition(), task.getObjectId());

                    this.currentPathIndex = 1; //a posição 0 é a casa onde o robo ja ta parado

                    gotTask = true;
                    break;


                }
            }
        }
        if (gotTask == false && this.getPosition().equals(this.chargingStation.getPosition()) == false) { // se nao apanhou nenhuma tarefa nem ta na estação
            this.state = RobotState.BUSY;
            this.phase = RobotPhase.GOING_TO_CHARGING_STATION;

            this.currentPath = CalculatePath.searchPath(project.getInitGrid(), this.getPosition(), this.chargingStation.getPosition(), -1); //pede o caminho para a estação //ID -1 porque nao estamos á procura de objetos
            this.currentPathIndex = 1;
        }

    }


    @Override
    public String toString() {
        String taskStr = "NONE";
        if (this.currentTask != null) {
            taskStr = "(O" + this.currentTask.getObjectId() + " --> " + this.currentTask.getEnd().getX() + ";" + this.currentTask.getEnd().getY() + ")";
        }

        String stateStr = this.state.toString();
        if (this.state == RobotState.BUSY && this.phase != RobotPhase.NONE) {
            stateStr = stateStr + " " + formatPhase(this.phase) + formatPathProgress();
        }

        return "[R" + this.id + "] pos=" + this.getPosition().getX() + ";" + this.getPosition().getY()
                + " charge=" + String.format("%.2f", this.charge) + "% state=" + stateStr + " task=" + taskStr;
    }

    private String formatPhase(RobotPhase phase) {
        switch (phase) {
            case GOING_TO_OBJECT:
                return "GOING TO OBJECT";
            case GOING_TO_DESTINATION:
                return "GOING TO DESTINATION";
            case GOING_TO_CHARGING_STATION:
                return "GOING TO CHARGING STATION";
            default:
                return "";
        }
    }

    private String formatPathProgress() {
        if (this.currentPath == null || this.currentPath.isEmpty()) {
            return "";
        }
        return " (" + this.currentPathIndex + "/" + this.currentPath.size() + ")";
    }
}
    


