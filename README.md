# TP2 - Instalação Robótica (Java)

Implementação simples e modular da simulação pedida no enunciado, com:
- leitura dos dados iniciais (`W H`, entidades e `start`);
- comandos `help`, `step`, `add-task`, `get-robot` e `exit`;
- robôs com estados `IDLE`, `BUSY`, `CHARGING`;
- pathfinding por DFS na ordem esquerda-cima-direita-baixo;
- validações principais de input e de comandos.

## Estrutura

- `src/Main.java`: entrada da aplicação.
- `src/InputParser.java`: valida e cria a simulação.
- `src/CommandLoop.java`: ciclo de comandos da consola.
- `src/Simulation.java`: estado global da simulação.
- `src/Robot.java`: comportamento e estado de cada robô.
- `src/PathFinder.java`: DFS para caminhos.
- restantes classes: modelos e enums (`Task`, `FactoryObject`, `Position`, etc.).

## Compilar

```cmd
javac -d out src\*.java
```

## Executar

```cmd
java -cp out Main
```

## Exemplo rápido de input

```text
7 5
OBS 2 3
OBS 2 4
OBS 3 4
OBS 6 2
ROB 1 4 1 2
OBJ 4 3
start
help
step
add-task 1 7 5
step
get-robot 1
exit
```
