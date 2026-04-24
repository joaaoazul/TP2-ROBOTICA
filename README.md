
## 1) `Main`

## Papel:

É o ponto de entrada do programa.

### O que faz:

*   cria o `InputReader`,
*   lê e valida os dados iniciais,
*   cria o `Project`,
*   entra no ciclo de comandos.

### O que não deve fazer:

*   não deve guardar lógica dos robôs,
*   não deve calcular caminhos,
*   não deve decidir tarefas,
*   não deve ter lógica de impressão complexa.

### Porque isto faz sentido?

Porque o enunciado diz que a classe `Main` deve conter apenas a leitura dos dados iniciais e a inicialização da simulação. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

***

# 2) `Project`

## Papel:

Esta passa a ser a tua classe central.  
É basicamente o “cérebro do sistema”.

### O que guarda:

*   o mapa (`InitGrid`),
*   a lista de robôs,
*   a lista de objetos,
*   a lista de obstáculos,
*   a lista de tarefas,
*   o contador de `steps`.

### O que faz:

*   executar um `step`,
*   imprimir o estado atual,
*   adicionar tarefas,
*   procurar um robô pelo ID,
*   processar comandos.

### Porque este nome faz sentido?

Mesmo não sendo o nome que eu sugeri antes (`Simulation`), `Project` pode funcionar se tu o entenderes como:

> “a classe que representa o estado geral do projeto/simulação”.

Se explicares assim ao professor, está bem defendido.

***

## Métodos que eu punha em `Project`

### Gestão geral

*   `runStep()`
*   `printState()`
*   `printMap()`
*   `printRobots()`
*   `printTasks()`

### Comandos

*   `showHelp()`
*   `addTask(int objectId, int destX, int destY)`
*   `getRobotById(int id)`
*   `processCommand(String commandLine)`

### Apoio

*   `getObjectById(int id)`
*   `isObjectAlreadyInTask(int objectId)`

***

# 3) `InitGrid`

## Papel:

É a classe que representa a grelha / instalação.

O enunciado diz que a instalação é representada por um mapa retangular finito, dividido em células em grelha, com regras específicas de representação visual (`..`, `##`, `R1`, `RR`, `O1`, `S1`, `C1`) e com coordenadas de linha e coluna. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

### O que guarda:

*   `width`
*   `height`

E opcionalmente pode guardar referências às entidades ou apenas fornecer métodos para trabalhar com posições.

### O que faz:

*   verificar se uma posição está dentro do mapa,
*   verificar se uma célula está ocupada,
*   verificar se uma célula está vazia,
*   saber se existe obstáculo/objeto/estação naquela posição,
*   ajudar a construir a representação visual do mapa.

### Porque `InitGrid` faz sentido?

O nome não é o mais comum, mas se para ti significa:

> “a grelha inicial/estrutura base da instalação”

…então serve.  
Só te aconselho a ter cuidado para que esta classe **não fique só para o início**. Se ela vai representar o mapa durante toda a execução, então na prática ela não é só “init”, ela é o mapa do projeto inteiro.

### Se o professor perguntar

Tu podes dizer:

> Usei o nome `InitGrid` para representar a grelha base da instalação, onde ficam definidas as dimensões e as regras de ocupação das células.

Isso é uma resposta aceitável.

***

## Métodos que eu punha em `InitGrid`

*   `isInside(Position position)`
*   `isCellFree(Position position, Project project)`
*   `hasObstacle(Position position, Project project)`
*   `hasObject(Position position, Project project)`
*   `hasChargingStation(Position position, Project project)`
*   `hasRobot(Position position, Project project)`
*   `render(Project project)`

### Nota importante

A `InitGrid` pode precisar de consultar as entidades que estão no `Project`, a menos que tu escolhas guardar tudo também dentro da própria `InitGrid`.

Há duas formas:

### Opção A — `Project` guarda as listas, `InitGrid` consulta o `Project`

Mais simples conceptualmente.

### Opção B — `InitGrid` guarda também listas internas

Pode funcionar, mas corres o risco de duplicar informação.

**Eu aconselho a Opção A**:

*   `Project` é o dono do estado geral,
*   `InitGrid` é o especialista na grelha.

***

# 4) `InputReader`

## Papel:

Ler e validar os dados iniciais.

Isto é uma escolha **muito boa**, porque o enunciado tem várias validações obrigatórias:

*   formato das dimensões, [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)
*   dimensões mínimas e máximas, [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)
*   tipo de entidade válido (`OBS`, `ROB`, `OBJ`), [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)
*   formato da entidade, [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)
*   coordenadas válidas e sem sobreposição, [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)
*   mínimo de entidades, robôs e objetos. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

### O que faz:

*   ler a primeira linha com dimensões,
*   validar essa linha,
*   ler as entidades até aparecer `start`,
*   validar cada entidade,
*   criar os objetos correspondentes,
*   devolver tudo pronto para o `Project`.

***

## O grande benefício do `InputReader`

Evitas isto:

*   `Main` gigante,
*   `Main` com `ifs` de validação por todo o lado,
*   criação de entidades misturada com lógica de erro.

E ficas com algo mais profissional:

*   `Main` arranca,
*   `InputReader` lê,
*   `Project` executa.

***

## Métodos que eu punha em `InputReader`

*   `Project readProject()`
*   `void readDimensions(...)`
*   `void readEntities(...)`
*   `Obstacle parseObstacle(...)`
*   `Robot parseRobot(...)`
*   `WarehouseObject parseObject(...)`
*   `boolean isValidEntityLine(...)`

### Outra possibilidade

Tu podes ter um método único:

*   `Project readFromScanner(Scanner scanner)`

E esse método faz tudo.

***

# 5) `CalculatePath`

## Papel:

Implementar o algoritmo de busca em profundidade (DFS).

O enunciado diz explicitamente que os robôs devem usar o algoritmo de busca em profundidade para calcular os caminhos necessários, com:

*   lista `visited`,
*   lista `path`,
*   ordem de exploração **esquerda, cima, direita, baixo**,
*   e backtracking quando não há caminho direto. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

### Porque faz sentido separar isto?

Porque calcular caminhos é uma responsabilidade específica e relativamente complexa.  
Se meteres isto dentro de `Robot`, a classe do robô fica enorme e difícil de entender.

### O que deve fazer:

*   receber ponto inicial,
*   receber destino,
*   saber o que é célula acessível,
*   devolver uma lista de posições,
*   ou indicar que não há caminho.

***

## Métodos que eu punha em `CalculatePath`

*   `List<Position> findPath(Position start, Position goal, Project project, WarehouseObject allowedObject)`
*   `boolean isAccessible(Position position, Position goal, Project project, WarehouseObject allowedObject)`
*   `List<Position> getNeighborsInOrder(Position current)`

### Porque `allowedObject` pode ser útil?

Porque o enunciado diz que uma célula acessível não pode ter objeto, **exceto o objeto que deve ser transportado**. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

Ou seja:

*   normalmente objeto bloqueia,
*   mas se o destino for o objeto que queres apanhar, essa célula tem de ser permitida.

Mesma lógica para a estação:

*   normalmente estação pode bloquear o caminho,
*   mas se o objetivo for chegar à estação, a célula objetivo tem de ser permitida.

***

# 6) `Position`

## Papel:

Representar coordenadas `(x, y)`.

O enunciado usa coordenadas no formato `x;y`, em que `x` representa a coluna e `y` a linha. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

### Campos:

*   `int x`
*   `int y`

### Métodos:

*   `getX()`
*   `getY()`
*   `equals()`
*   `hashCode()`
*   `toString()`

### Porque esta classe é obrigatória na prática?

Porque senão vais passar a vida a carregar pares de inteiros soltos em todo o lado.

***

# 7) `Entity` e subclasses

O enunciado define 4 tipos de entidades no mapa:

*   obstáculo,
*   robô,
*   estação de carregamento,
*   objeto. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

Aqui faz muito sentido usar herança.

***

## `Entity` (abstrata)

## Papel:

Classe base para tudo o que ocupa uma posição no mapa.

### Campo:

*   `Position position`

### Métodos:

*   `getPosition()`
*   `setPosition(Position position)`

***

## `Obstacle extends Entity`

## Papel:

Representar um obstáculo fixo.

### Observações:

*   não tem ID,
*   não se move,
*   bloqueia caminho. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

***

## `WarehouseObject extends Entity`

(ou outro nome que não seja `Object`)

## Papel:

Representar os objetos transportáveis.

### Campos:

*   `int id`
*   `boolean beingCarried`

### Porque isto faz sentido?

Porque os objetos:

*   têm ID atribuído por ordem de criação, [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)
*   podem estar visíveis no mapa,
*   ou podem estar a ser carregados e deixar de aparecer. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

***

## `ChargingStation extends Entity`

## Papel:

Representar a estação de carregamento de um robô.

### Campo:

*   `int robotId`

### Porque é preciso isto?

Porque cada robô tem a sua própria estação e só pode usar a sua. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

***

## `Robot extends Entity`

## Papel:

Representar o comportamento autónomo do robô.

### Campos principais:

*   `int id`
*   `double charge`
*   `RobotState state`
*   `RobotPhase phase`
*   `ChargingStation chargingStation`
*   `Task currentTask`
*   `WarehouseObject carriedObject`
*   `List<Position> currentPath`
*   `int currentPathIndex`

### Porque esta classe é a mais importante?

Porque o robô é quem:

*   escolhe tarefas,
*   calcula se tem bateria suficiente,
*   usa o DFS,
*   anda no mapa,
*   pega/larga objetos,
*   vai carregar. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

***

# 8) Estados do robô

O enunciado indica os estados principais:

*   `IDLE`
*   `BUSY`
*   `CHARGING` [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

E também mostra subestados de movimento:

*   `GOING TO OBJECT`
*   `GOING TO DESTINATION`
*   `GOING TO CHARGING STATION` [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

Portanto, eu usaria dois enums.

***

## `RobotState`

```java
public enum RobotState {
    IDLE,
    BUSY,
    CHARGING
}
```

***

## `RobotPhase`

```java
public enum RobotPhase {
    NONE,
    GOING_TO_OBJECT,
    GOING_TO_DESTINATION,
    GOING_TO_CHARGING_STATION
}
```

### Porque separar?

Porque:

*   `state` diz o estado principal,
*   `phase` dá detalhe do que está a acontecer naquele momento.

Isto torna o código mais limpo do que usar strings soltas.

***

# 9) `Task`

## Papel:

Representar uma tarefa criada pelo utilizador com `add-task <obj-id> <dest-x> <dest-y>`. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

### Campos:

*   `WarehouseObject object`
*   `Position destination`
*   `TaskStatus status`
*   `Robot assignedRobot`

### Métodos:

*   `isFree()`
*   `assignTo(Robot robot)`
*   `markCompleted()`

***

## `TaskStatus`

```java
public enum TaskStatus {
    FREE,
    ASSIGNED,
    COMPLETED
}
```

### Porque existe esta classe?

Porque as tarefas são entidades lógicas do sistema:

*   ficam numa lista,
*   são criadas pelo utilizador,
*   podem ser atribuídas a um robô,
*   podem ser concluídas. [\[Trabalho 2...o Robótica \| PDF\]](https://myresources.deloitte.com/personal/maaquino_deloitte_pt/Documents/Microsoft%20Copilot%20Chat%20Files/Trabalho%202%20-%20Instala%C3%A7%C3%A3o%20Rob%C3%B3tica.pdf)

***

# 10) Como tudo fica ligado

Com os nomes que escolheste, eu vejo a arquitetura assim:

```text
Main
 └── usa InputReader
      └── cria Project

Project
 ├── tem InitGrid
 ├── tem List<Robot>
 ├── tem List<WarehouseObject>
 ├── tem List<Obstacle>
 ├── tem List<Task>
 └── usa CalculatePath quando necessário

Entity
 ├── Obstacle
 ├── WarehouseObject
 ├── ChargingStation
 └── Robot

Robot
 ├── tem ChargingStation
 ├── tem Task atual
 ├── pode transportar WarehouseObject
 └── usa CalculatePath para obter caminhos

Task
 ├── refere WarehouseObject
 └── pode estar associada a Robot

Position
 └── usada por quase todas as classes
```

***

# 11) Distribuição de responsabilidades (isto é MESMO importante)

Uma boa regra para não te perderes é esta:

## `Main`

**Só arranca**

## `InputReader`

**Lê e valida**

## `Project`

**Controla o sistema**

## `InitGrid`

**Controla regras da grelha**

## `CalculatePath`

**Calcula caminhos**

## `Robot`

**Executa comportamento do robô**

## `Task`

**Representa pedidos do utilizador**

## `Position`

**Representa coordenadas**

Se respeitares isto, o teu código fica muito mais fácil de entender.

***

# 12) A única coisa que eu te alertava nos nomes

As tuas escolhas são válidas, mas há 2 nomes que podem gerar ligeira ambiguidade:

## `Project`

Pode parecer o projeto inteiro em vez da simulação.  
Mas se tu fores consistente, funciona.

## `InitGrid`

Pode soar a “grelha inicial apenas”.  
Se esta classe vai viver durante o programa todo, talvez o nome não seja o mais intuitivo.

### MAS:

Se já escolheste esses nomes e te sentes confortável, **não há problema**.  
O importante é tu saberes explicar.

***

# 13) Se eu convertesse a tua ideia numa lista final de classes, ficava assim

## Classes principais

*   `Main`
*   `Project`
*   `InputReader`
*   `InitGrid`
*   `CalculatePath`

## Classes de domínio

*   `Position`
*   `Task`

## Hierarquia de entidades

*   `Entity`
*   `Obstacle`
*   `WarehouseObject`
*   `ChargingStation`
*   `Robot`

## Enums

*   `RobotState`
*   `RobotPhase`
*   `TaskStatus`


