import java.util.List;
import java.util.Objects;

import aima.core.search.adversarial.Game;

public class NineMensMorrisGame implements Game<NineMensMorrisState, Move, Token>{
    
    private NineMensMorrisState initialState = new NineMensMorrisState();

    @Override
    public NineMensMorrisState getInitialState() {
        return initialState;
    }

    @Override
    public Token[] getPlayers() {
        return new Token[] {Token.PLAYER_1, Token.PLAYER_2};
    }

    @Override
    public Token getPlayer(NineMensMorrisState state) {
        return state.getPlayerToMove();
    }

    @Override
    public List<Move> getActions(NineMensMorrisState state) {
        return state.generateMoves();
    }

    @Override
    public NineMensMorrisState getResult(NineMensMorrisState state, Move action) {
        NineMensMorrisState result = state.clone();
        result.applyMove(action);
        return result;
    }

    @Override
    public boolean isTerminal(NineMensMorrisState state) {
        return state.getUtility() != -1;
    }

    @Override
    public double getUtility(NineMensMorrisState state, Token player) {
        double result = state.getUtility();
        if (result != -1) {
            if (Objects.equals(player, Token.PLAYER_2))
                result = 1 - result;
        } else {
            throw new IllegalArgumentException("Estado não terminal.");
        }
        return result;
    }

    @Override
    public int getGamePhase(NineMensMorrisState state){
       
        return state.getCurrentGamePhase();
    }
    
    @Override
    public double euristics(NineMensMorrisState state){
       
        Token player_1 = Token.PLAYER_1;
        Token player_2 = Token.PLAYER_2;
        
        int peca_1 = 0, peca_2 = 0;

        peca_1 = state.getNumPiecesPlayer(player_1);
        peca_2 = state.getNumPiecesPlayer(player_2);
        
        return 1-((Math.abs(peca_1 - peca_2))/9);
    }
    
}
