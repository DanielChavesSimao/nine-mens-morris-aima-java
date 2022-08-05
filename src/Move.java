public class Move {
    static public final int PLACING = 1;
	static public final int MOVING = 2;
	static public final int REMOVING = 3;
	
	public int srcIndex, destIndex, removePieceOnIndex;
	public final int typeOfMove;
	public int score;
	
	public Move(int src, int dest, int remove, int type) throws GameException {
		if(type != PLACING && type != MOVING && type != REMOVING) {
			throw new GameException(getClass().getName()+" - Tipo de movimento inválido.");
		}
		this.srcIndex = src;
		this.destIndex = dest;
		this.removePieceOnIndex = remove;
		this.typeOfMove = type;
	}

	@Override
	public String toString() {
		return "Move("+srcIndex+","+destIndex+","+removePieceOnIndex+","+typeOfMove+")";
	}
}
