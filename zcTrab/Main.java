package aimaDaniel.trilha.zcTrab;

import java.io.BufferedReader;
import java.io.InputStreamReader;


import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;

public class Main {
    public static void main(String[] args) {
        try {
            // testeGame();
            minimaxAIGame();
        } catch (GameException e) {
            e.printStackTrace();
        }
    }

    private static void testeGame() throws GameException {
        System.out.println("Teste");
        NineMensMorrisGame game = new NineMensMorrisGame();
        NineMensMorrisState currState = game.getInitialState();
        currState.printGameBoard();
        // IterativeDeepeningAlphaBetaSearch<NineMensMorrisState, Move, Token> search = IterativeDeepeningAlphaBetaSearch.createFor(game,0.0,1.0,300);
        // search.setLogEnabled(true);
        while (!(game.isTerminal(currState))) {
            System.out.println(game.getPlayer(currState) + " bolando a play...");
            currState = userPlay(game, currState);
            // Move action = search.makeDecision(currState);
            // currState = game.getResult(currState, action);
            System.out.println("Lance do software");
            // System.out.println(currState.toString());
            currState.printGameBoard();
            if (!game.isTerminal(currState)) {
                currState = userPlay(game, currState);
                System.out.println("Lance do usuario");
                currState.printGameBoard();
            }
        }
        System.out.println("Teste fim.");
    }

    private static void minimaxAIGame() throws GameException {
        System.out.println("Teste");
            NineMensMorrisGame game = new NineMensMorrisGame();
            NineMensMorrisState currState = game.getInitialState();
            currState.printGameBoard();
            IterativeDeepeningAlphaBetaSearch<NineMensMorrisState, Move, Token> search = IterativeDeepeningAlphaBetaSearch.createFor(game,0.0,1.0,10);
            // search.setLogEnabled(true);
            while (!(game.isTerminal(currState))) {
                System.out.println(game.getPlayer(currState) + " bolando a play...");
                Move action = search.makeDecision(currState);
                currState = game.getResult(currState, action);
                System.out.println("Lance do software");
                // System.out.println(currState.toString());
                currState.printGameBoard();
                if (!game.isTerminal(currState)) {
                    currState = userPlay(game, currState);
                    System.out.println("Lance do usuario");
                    currState.printGameBoard();
                }
            }
            System.out.println("Teste fim.");
    }

    private static NineMensMorrisState userPlay(NineMensMorrisGame game, NineMensMorrisState state) throws GameException {
        int x,y;
        if (state.getCurrentGamePhase() == NineMensMorrisState.PLACING_PHASE) {
            System.out.println("Selecione uma posicao");
            x = readNum2();
            Move uaction = new Move(-7, x, -1, Move.PLACING);
            state = game.getResult(state,uaction);            
            if (state.madeAMill(x, Token.PLAYER_2)) {
                System.out.println("Selecione uma peca para remover");
                x = readNum2();
                Move removeAction = new Move(-7, -1, x, Move.REMOVING);
                state = game.getResult(state, removeAction);
            }
        } else {
            System.out.println("Selecione uma peca pra mover");
            x = readNum2();
            System.out.println("Selecione uma posicao");
            y = readNum2();
            Move uaction = new Move(x, y, -1, Move.MOVING);
            state = game.getResult(state,uaction);
            if (state.madeAMill(x, Token.PLAYER_2)) {
                System.out.println("Selecione uma peca para remover");
                x = readNum2();
                Move removeAction = new Move(-7, -1, x, Move.REMOVING);
                state = game.getResult(state, removeAction);
            }
        }
        
        return state;
    }

    private static int readNum2() {
        int i ;
        String s;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
          s = br.readLine();
        } catch(java.io.IOException e) {
            return 1;
        }
        try{
            i = Integer.parseInt(s);
        }catch(NumberFormatException nfe){
            return 1;
        }
        return i;
    }
}
