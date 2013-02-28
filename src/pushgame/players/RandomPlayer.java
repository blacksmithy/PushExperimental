package pushgame.players;

import java.util.ArrayList;
import java.util.List;

import pushgame.logic.Board;
import pushgame.logic.Movement;

public class RandomPlayer extends Player {

	public RandomPlayer(byte id, int delay) {
		super(id, delay);
		// TODO Auto-generated constructor stub
		
		this.statsMovesNum = 1;
		this.statsVisitedNodes = 0;
	}

	@Override
	public Movement makeMove(Board board) {
		List<Movement> moves = board.getPossibleMoves(id);
		
		statsVisitedNodes += moves.size();
		statsMovesNum++;
		
		if (!board.hasForwardMoves(id)) { // LOCK PREVENTION
			boolean nextMoveForward = false;
			List<Movement> forwardMoves = new ArrayList<Movement>();
			for (Movement m : moves) {
				if ((m.getAngle() == 6 || m.getAngle() == 2)
						&& board.getBoardCopyAfterMove(m).hasForwardMoves(id)) {
					nextMoveForward = true;
					forwardMoves.add(m);
				}
			}
			if (!forwardMoves.isEmpty()) {
				moves = forwardMoves;
			}

			if (!nextMoveForward) { // UNLOCK ENEMY
				List<Movement> moves3 = new ArrayList<Movement>();
				for (Movement m : moves) {
					if (board.getBoardCopyAfterMove(m).hasForwardMoves((byte) (3 - id))) {
						moves3.add(m);
					}
				}
				if (!moves3.isEmpty()) {
					moves = moves3;
				}
			}
		}
		
		Movement move = moves.get(0);
		return move;
	}

}
