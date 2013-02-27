package pushgame.players;

import pushgame.logic.Board;
import pushgame.logic.Movement;

public class RandomPlayer extends Player {

	public RandomPlayer(byte id, int delay) {
		super(id, delay);
		// TODO Auto-generated constructor stub
		
		this.statsMovesNum = 1;
		this.statsVisitedNodes = 1;
	}

	@Override
	public Movement makeMove(Board board) {
		Movement move = board.getPossibleMoves(id).get(0);
		return move;
	}

}
