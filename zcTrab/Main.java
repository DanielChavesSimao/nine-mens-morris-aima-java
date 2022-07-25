package aimaDaniel.trilha.zcTrab;

import java.io.BufferedReader;
import java.io.InputStreamReader;


import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Teste");
            NineMensMorrisGame game = new NineMensMorrisGame();
            NineMensMorrisState currState = game.getInitialState();
            currState.printGameBoard();
            IterativeDeepeningAlphaBetaSearch<NineMensMorrisState, Move, Token> search = IterativeDeepeningAlphaBetaSearch.createFor(game,0.0,1.0,300);
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
        } catch (GameException e) {
            e.printStackTrace();
        }
    }

    private static NineMensMorrisState userPlay(NineMensMorrisGame game, NineMensMorrisState state) throws GameException {
        if (state.getCurrentGamePhase() == NineMensMorrisState.PLACING_PHASE) {
            System.out.println("Selecione uma posicao");
            int x = readNum2();
            Move uaction = new Move(-7, x, 0, Move.PLACING);
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
