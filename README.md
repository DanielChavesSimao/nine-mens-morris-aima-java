# nine-mens-morris-aima-java

# Trabalho 1 Inteligência Computacional 2022 - Trilha

## Equipe:
Fábio Cesar Polinski
Daniel Chaves Simão

## Descrição da arquitetura
Nossa implementação do jogo trilha foi feita utilizando a biblioteca da AIMA em Java.

### Board
Na classe Board temos a criação dos elementos do tabuleiro, que no nosso caso são printados via texto no terminal. A função clone cria uma cópio do estado do tabuleiro. A função getPosition retorna a posição do tabuleiro. A função positionIsAvailable verifica que a posição que está se querendo jogar está disponível. A função setPositionAsPlayer seta uma posição do tabuleiro como sendo de um player(colocar uma peça la). As funções incNumTotalPiecesPlaced, incNumPiecesOfPlayer e devNumPiecesOfPlayer são funções para incrementar o número total de peças no tabuleiro, incrementar número total de peças de um player e decrementar número total de peças de um player, respectivamente. As funções getNumberOfPiecesOfPlayer e getNPiecesPlayer servem paara retornar o valor de peças que um player tem no tabuleiro. A função initBoard inicializa o tabuleiro, com seus 3 quadrados(outer, middle e inner).  A função getMillCombination serve para pegar a combinação de casas que gera uma trilha. A função initMillCombinations tem todasa as combinações possíveis de trilhas. A função printBoard e a String toString printam o tabuleiro. A função showPos mostra quais posições estão com qual player, onde o Player_1(IA) é denotada por X, o Player_2(Humano) é denotado por O e as casas vazias são denotadas por *. A função getNumTotalPiecesPlaced retorna o valor total de peças colocadas no tabuleiro. A função equals verifica se 2 objetos são igausi. A função hashcode precisa garantir que objetos iguais tenham hashcodes equivalentes.

### GameException
A classe GameException é apenas uma classe de exceções, que retorna uma string com a exceção encontrada.

### Move
Na classe Move temos o model dos movimentos. Os tipos de movimentos que podem ser feitos no jogo: de colocar peças(1), mover peças(2) e remover peças(3). La temos seu construtor, que recebe a origem de seu movimento, seu destino, se tiver uma peça no index para remover e o tipo de movimento.

### Position
Na classe Position, temos o model das posições. Nelas temos variáveis para saber se a posição está ocupada, seu index e se estiver ocupada qual player está ocupando-a e seus getters. As funções setAsOccupied e setAsUnoccupied setam a posição como ocupada ou inocupada, respectivamente. As funções addAdjacentPositionIndexes adicionam as posições adjacentes a posição e a função getAdjacentPositionsIndex retorna essas posições. A função isAdjacentToThis verifica se a posição é adjacente a outra.

### Token
Na enumeração Token temos os players do jogo, onde Player_1 é a IA, Player_2 é o Humano e No_Player é composto pelas casas vazias.

### Game
A interface da aima Game teve que ter duas funções adicionadas para o cálculo da funçãao heurística, que são getGamePhase e getNumPiecesPlayer.

### NineMansMorrisGame
A classe NineMansMorrisGame implementa a interface Game, ou seja, temos todas as funções da classe Game, adaptadas para o jogo da Trilha. 

### NineMansMorrisState
Na classe NineMansMorrisState temos os estados do jogo e as regras. Nela temos a deifinição de 3 fases de jogo, a 1ª sendo a de posicionamento das peças no tabuleiro, a 2ª a de movimentação após todas as peças terem sido colocadas e a 3ª quando um jogador tem apenas 3 peças. Nessa classes, temos funções para retornarmos qual a fase atual do jogo(getCurrentGamePhase), a utilidade dos nós(getUtility), qual player deve-se mover(getPlayerToMove), seu tabuleiro(getGameBoard), a posição de um player no tabuleiro(getPlayerInBoardPosition), quantas peças um player tem no tabuleiro(getNumPiecesPlayer), se uma movimentação é válida, inválida ou se a posição está disponível(movePieceFromTo) e quantas peças um player tem em sequencia(numPiecesFromPlayerInRow).
Temos também funções para checar(checkMove), aplicar(applyMove) e gerar(generateMoves) os movimentos realizados. 
Temos funções de verificação, onde verificamaos se uma posição está disponível(positionIsAvailable), se um movimento é válido(validMove), se o posicionamento de um player é válido(placePieceOfPlayer),  se um moinho foi feito(madeAMill), se uma posição ja tem uma peça(positionHasPieceOfPlayer), se é permito remover uma peça(removePiece), se o jogo acabou(isTheGameOver) e se objetos são iguais(equals e hashCode).
Também temos uma função que printa o tabuleiro: printGameBoard.

### Main
Na classe Main temos a execução da nossa aplicação, onde temos o procedimento de busca de minimax com podas alfa-beta implementado na aima.
Essa busca está estendida para uma profundidade de busca a 7 e uma função heurística para estimar a utilidade dos nós não terminais.
Ao rodar essa classe, criamos o jogo chamando a função minimaxAIGame.
Nessa função temos a criação do game, de seu state e de sua busca, assim como o print do tabuleiro. Enquanto o jogo não terminar, ele vai executar e fazer os Moves do player que tiver vez, assim atualizando seu state e printando o tabuleiro. Assim, para o usuario jogar, temos que fazer uma função userPlay. Nela o usuário digita o índice via texto da posição que quer jogar, se estiver na fase inicial, ele posiciona as peças nas posições desejadas e se fizer uma trilha pode reomver uma do adversário. Caso esteja nos outros estados, seleciona uma de suas peças e as move, caso forme trilha pode remover uma do adversário.
Temos também a função readNum2, que lê o número inserido pelo humano.

### IterativeDeepeningAlphaBetaSearch
A busca com podas alfa-beta foi estendida para limitar o número de profundidade de busca para 7. Para isso, apenas definimos no while de makeDecision um critério de parada, enquanto a depth atual for < que 7, ele irá procurar nas árvores, caso contrário ele para.
Para a função heurística(eval) analisamos a utilidade dos nós não terminais, levando em consideração aspectos do estado do jogo. No nosso caso levamos em consideração a fase em que o jogo se encontra e quantas peças o player tem no tabuleiro.
Caso essa função venha a ser usada, temos um critério de para no while da função makeDecision, que enquanto a função heuristica for usada o while roda.
eval = gamephase*numpieces*((utilMin + utilMax) / gamephase);
