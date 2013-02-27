package pushgame.players;

import java.util.List;

import pushgame.logic.Board;
import pushgame.logic.Movement;
import pushgame.oracle.DistancesOracle;
import pushgame.oracle.Oracle;

public class GreedyPlayer extends Player {

	private static final byte player1 = 1;
	private static final byte player2 = 2;
	
	public GreedyPlayer(byte id, int delay) {
		super(id, delay);
		
		this.statsMovesNum = 1;
		this.statsVisitedNodes = 1;
	}

	@Override
	public Movement makeMove(Board board) {
		Movement decision = null;
		List<Movement> moves = board.getPossibleMoves(id);
		
		List<Movement> lockPreventionMoves = null;
		Oracle oracle = new DistancesOracle();
		decision = moves.get(0);
		
		// 7 | 0 | 1
		// 6 | X | 2
		// 5 | 4 | 3
		if (id == player1 && ((decision.getAngle() != 5) && (decision.getAngle() != 4) && (decision.getAngle() != 3))) {
			System.out.println("LOCK! @ " + decision.getAngle());
			lockPreventionMoves = board.resolveLock(id);
			if (! lockPreventionMoves.isEmpty()) {
				moves = lockPreventionMoves;
			}
		}
		else if (id == player2 &&  ((decision.getAngle() != 7) && (decision.getAngle() != 0) && (decision.getAngle() != 1))) {
			System.out.println("LOCK! @ " + decision.getAngle());
			lockPreventionMoves = board.resolveLock(id);
			if (! lockPreventionMoves.isEmpty()) {
				moves = lockPreventionMoves;
			}
		}
		
		short max = Short.MIN_VALUE;
		short val = 0;
		for (Movement m : moves) {
			val = oracle.getProphecy(null, board.getBoardCopyAfterMove(m), m, id);
			if (val > max) {
				max = val;
				decision = m;
			}
		}

		return decision;
	}

}
