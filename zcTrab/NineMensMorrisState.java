package aimaDaniel.trilha.zcTrab;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NineMensMorrisState implements Cloneable{
    static public final int NUM_PIECES_PER_PLAYER = 9;
	static public final int PLACING_PHASE = 1;
	static public final int MOVING_PHASE = 2;
	static public final int FLYING_PHASE = 3;
	
	static public final int INVALID_SRC_POS = -1;
	static public final int UNAVAILABLE_POS = -2;
	static public final int INVALID_MOVE = -3;
	static public final int VALID_MOVE = 0;

	static protected final int MIN_NUM_PIECES = 2;
	
	protected Board gameBoard = new Board();
	protected int gamePhase = PLACING_PHASE;

	private Token playerToMove = Token.PLAYER_1;
	private double utility = -1;

	public Token getPlayerToMove() {
		return playerToMove;
	}

	public double getUtility() {
		return utility;
	}

    public int getCurrentGamePhase() {
		return gamePhase;
	}
	
	public Board getGameBoard() {
		return gameBoard;
	}

	@Override
	public NineMensMorrisState clone() {
		NineMensMorrisState copy = null;
		try {
			copy = (NineMensMorrisState) super.clone();
			copy.gameBoard = (Board) gameBoard.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace(); // should never happen...
		}
		return copy;
	}

	public void applyMove(Move move) {
		try{
			Token opponent = getPlayerToMove() == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1;
			Position position = getGameBoard().getPosition(move.destIndex);
			position.setAsOccupied(getPlayerToMove());

			if (getCurrentGamePhase() == PLACING_PHASE) {
				getGameBoard().incNumPiecesOfPlayer(getPlayerToMove());
			} else {
				getGameBoard().getPosition(move.srcIndex).setAsUnoccupied();
			}

			if (move.removePieceOnIndex != -1) {
				Position removed = getGameBoard().getPosition(move.removePieceOnIndex);
				removed.setAsUnoccupied();
				getGameBoard().decNumPiecesOfPlayer(opponent);
			}

			playerToMove = opponent;
		} catch (GameException e) {
			e.printStackTrace();
		}
	}

	private void checkMove(Board gameBoard, Token player, List<Move> moves, Move move) throws GameException {
		boolean madeMill = false;
		for(int i = 0; i < Board.NUM_MILL_COMBINATIONS; i++) { //check if piece made a mill
			int playerPieces = 0; 
			boolean selectedPiece = false;
			Position[] row = gameBoard.getMillCombination(i);

			for(int j = 0; j < Board.NUM_POSITIONS_IN_EACH_MILL; j++) {

				if(row[j].getPlayerOccupyingIt() == player) {
					playerPieces++;
				}
				if(row[j].getPositionIndex() == move.destIndex) {
					selectedPiece = true;
				}
			}

			if(playerPieces == 3 && selectedPiece) { // made a mill - select piece to remove
				madeMill = true;

				for(int l = 0; l < Board.NUM_POSITIONS_OF_BOARD; l++) {
					Position pos = gameBoard.getPosition(l);

					if(pos.getPlayerOccupyingIt() != player && pos.getPlayerOccupyingIt() != Token.NO_PLAYER) {
						move.removePieceOnIndex = l;

						// add a move for each piece that can be removed, this way it will check what's the best one to remove
						moves.add(move);
						//movesThatRemove++; // TODO TESTING
					}
				}
			}
			selectedPiece = false;					
		}

		if(!madeMill) { // don't add repeated moves
			moves.add(move);
		} else {
			madeMill = false;
		}
	}

	public List<Move> generateMoves() {
		List<Move> moves = new ArrayList<Move>();
		Position position, adjacentPos;
		Token player = getPlayerToMove();

		try {
			if (gamePhase == PLACING_PHASE) {
				for (int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) {
					Move move = new Move(-7, -1, -1, Move.PLACING);

					if (!(position = getGameBoard().getPosition(i)).isOccupied()) {
						position.setAsOccupied(player);
						move.destIndex = i;
						checkMove(gameBoard, player, moves, move);
						position.setAsUnoccupied();
					}
				}
			} else if (gamePhase == MOVING_PHASE) {
				for(int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) {

					if((position = gameBoard.getPosition(i)).getPlayerOccupyingIt() == player) { // for each piece of the player
						int[] adjacent = position.getAdjacentPositionsIndexes();

						for(int j = 0; j < adjacent.length; j++) { // check valid moves to adjacent positions
							Move move = new Move(i, -1, -1, Move.MOVING);
							adjacentPos = gameBoard.getPosition(adjacent[j]);

							if(!adjacentPos.isOccupied()) {
								adjacentPos.setAsOccupied(player);
								move.destIndex = adjacent[j];
								position.setAsUnoccupied();
								checkMove(gameBoard, player, moves, move);
								position.setAsOccupied(player);
								adjacentPos.setAsUnoccupied();
							}
						}
					}
				}
			} else if (gamePhase == FLYING_PHASE) {
				List<Integer> freeSpaces = new ArrayList<Integer>();
				List<Integer> playerSpaces = new ArrayList<Integer>();

				for(int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) {
					if((position = gameBoard.getPosition(i)).getPlayerOccupyingIt() == player) {
						playerSpaces.add(i);
					} else if(!position.isOccupied()) {
						freeSpaces.add(i);
					}
				}

				// for every piece the player has on the board
				for(int n = 0; n < playerSpaces.size(); n++) {
					Position srcPos =  gameBoard.getPosition(playerSpaces.get(n));
					srcPos.setAsUnoccupied();

					// each empty space is a valid move
					for(int j = 0; j < freeSpaces.size(); j++) {
						Move move = new Move(srcPos.getPositionIndex(), -1, -1, Move.MOVING);
						Position destPos = gameBoard.getPosition(freeSpaces.get(j));
						destPos.setAsOccupied(player);
						move.destIndex = freeSpaces.get(j);
						checkMove(gameBoard, player, moves, move);
						destPos.setAsUnoccupied();
					}
					srcPos.setAsOccupied(player);
				}
			}
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		

		return moves;
	}

    public Token getPlayerInBoardPosition(int boardPosition) {
		try {
			return gameBoard.getPosition(boardPosition).getPlayerOccupyingIt();
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return Token.NO_PLAYER;
	}

    public boolean positionIsAvailable(int boardIndex) throws GameException {
        return gameBoard.positionIsAvailable(boardIndex);
	}

    public boolean validMove(int currentPositionIndex, int nextPositionIndex) throws GameException {
		Position currentPos = gameBoard.getPosition(currentPositionIndex);
		if(currentPos.isAdjacentToThis(nextPositionIndex) && !gameBoard.getPosition(nextPositionIndex).isOccupied()) {
			return true;
		}
		return false;
	}

    public int movePieceFromTo(int srcIndex, int destIndex, Token player) throws GameException {
		if(positionHasPieceOfPlayer(srcIndex, player)) {
			if(positionIsAvailable(destIndex)) {
				//System.out.println("Number of pieces: "+gameBoard.getNumberOfPiecesOfPlayer(player));
				if(validMove(srcIndex, destIndex) || (gameBoard.getNumberOfPiecesOfPlayer(player) == MIN_NUM_PIECES + 1)) {
					gameBoard.getPosition(srcIndex).setAsUnoccupied();
					gameBoard.getPosition(destIndex).setAsOccupied(player);
					return VALID_MOVE;
				} else {
					return INVALID_MOVE;
				}
			} else {
				return UNAVAILABLE_POS;
			}
		} else {
			return INVALID_SRC_POS;
		}
	}

    public boolean placePieceOfPlayer(int boardPosIndex, Token player) throws GameException {
		if(gameBoard.positionIsAvailable(boardPosIndex)) {
			gameBoard.getPosition(boardPosIndex).setAsOccupied(player);
			gameBoard.incNumPiecesOfPlayer(player);
			if(gameBoard.incNumTotalPiecesPlaced() == (NUM_PIECES_PER_PLAYER * 2)) {
				gamePhase = MOVING_PHASE;
			}
			return true;
		}
		return false;
	}

    public boolean madeAMill(int dest, Token player) throws GameException {
		int maxNumPlayerPiecesInRow = 0;
		for(int i = 0; i < Board.NUM_MILL_COMBINATIONS; i++) {
			Position[] row = gameBoard.getMillCombination(i);
			for(int j = 0; j < Board.NUM_POSITIONS_IN_EACH_MILL; j++) {
				if(row[j].getPositionIndex() == dest) {
					int playerPiecesInThisRow = numPiecesFromPlayerInRow(row, player);
					if(playerPiecesInThisRow > maxNumPlayerPiecesInRow) {
						maxNumPlayerPiecesInRow = playerPiecesInThisRow;
					}
				}
			}
		}
		return (maxNumPlayerPiecesInRow == Board.NUM_POSITIONS_IN_EACH_MILL);
	}

    private int numPiecesFromPlayerInRow(Position[] pos, Token player) {
		int counter = 0;
		for(int i = 0; i < pos.length; i++) {
			if(pos[i].getPlayerOccupyingIt() == player) {
				counter++;
			}
		}
		return counter;
	}

    public boolean positionHasPieceOfPlayer(int boardIndex, Token player) throws GameException {
		return (gameBoard.getPosition(boardIndex).getPlayerOccupyingIt() == player);
	}

    public void printGameBoard() {
		gameBoard.printBoard();
	}

    public boolean removePiece(int boardIndex, Token player) throws GameException { 
		if(!gameBoard.positionIsAvailable(boardIndex) && positionHasPieceOfPlayer(boardIndex, player)) {
			gameBoard.getPosition(boardIndex).setAsUnoccupied();
			gameBoard.decNumPiecesOfPlayer(player);
			if(gamePhase == MOVING_PHASE && gameBoard.getNumberOfPiecesOfPlayer(player) == (MIN_NUM_PIECES+1)) {
				gamePhase = FLYING_PHASE;
				System.out.println("Nova fase: "+gamePhase);
			}
			return true;
		}
		return false;
	}

    public boolean isTheGameOver() {
		try {
			if(gameBoard.getNumberOfPiecesOfPlayer(Token.PLAYER_1) == MIN_NUM_PIECES
					|| gameBoard.getNumberOfPiecesOfPlayer(Token.PLAYER_2) == MIN_NUM_PIECES) {
				return true;
			} else {
				boolean p1HasValidMove = false, p2HasValidMove = false;
				Token player;
				
				// check if each player has at least one valid move
				for(int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) {
					Position position = gameBoard.getPosition(i);
					if((player = position.getPlayerOccupyingIt()) != Token.NO_PLAYER) {
						int[] adjacent = position.getAdjacentPositionsIndexes();
						for(int j = 0; j < adjacent.length; j++) {
							Position adjacentPos = gameBoard.getPosition(adjacent[j]);
							if(!adjacentPos.isOccupied()) {
								if(!p1HasValidMove) { // must only change if boolean is false
									p1HasValidMove = (player == Token.PLAYER_1);
								}
								if(!p2HasValidMove) {
									p2HasValidMove = (player == Token.PLAYER_2);
								}
								break;
							}
						}
					}
					if(p1HasValidMove && p2HasValidMove) {
						return false;
					}
				}
			}
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return true;
	}

	@Override
	public boolean equals(Object anObj) {
		if (anObj != null && anObj.getClass() == getClass()) {
			NineMensMorrisState anotherState = (NineMensMorrisState) anObj;
			for (int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) {
				try {
					if (!Objects.equals(gameBoard.getPosition(i), anotherState.gameBoard.getPosition(i)))
						return false;
				} catch (GameException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// Need to ensure equal objects have equivalent hashcodes (Issue 77).
		return toString().hashCode();
	}
}
