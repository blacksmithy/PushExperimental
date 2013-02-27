package pushgame.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pushgame.logic.Board;
import pushgame.logic.Movement;
import pushgame.oracle.Oracle;
import pushgame.oracle.SymmetricDistancesOracle;
import pushgame.util.GameConfig;
import pushgame.util.MovementComparator;
import pushgame.util.Transposition;
import pushgame.util.TranspositionTable;

public class CstarPlayer extends Player {

	private Oracle oracle;
	private TranspositionTable tt1;
	private TranspositionTable tt2;
	private Board board;

	private int firstMoves = 0;
	private int firstMovesNum = 2;
	private boolean sortEnable = true;	

	public CstarPlayer(byte id, int delay) {
		super(id, delay);
		oracle = new SymmetricDistancesOracle();
		
		this.statsMovesNum = 1;
		this.statsVisitedNodes = 1;
	}

	private short cStar(Board inputBoard, short depth, byte player) {
		short fPlus = Short.MAX_VALUE;
		short fMinus = Short.MIN_VALUE;
		short gamma = 0;
		short value = 0;

		while (fPlus != fMinus) {
			gamma = (short) Math.ceil(((double) (fPlus + fMinus) / 2));
			value = (short) alphaBeta(inputBoard, depth, (short) (gamma - 1),
					gamma, player);
			if (value < gamma) {
				fPlus = value;
			} else {
				fMinus = value;
			}
		}

		return value;
	}

	private short alphaBeta(Board inputBoard, short depth, short alpha,
			short beta, byte player) {

		if ((depth == 0) || inputBoard.getWinner() != 0) {
			return this.oracle
					.getProphecy(this.board, inputBoard, null, player);
		}
		
		List<Movement> moves;// = inputBoard.getPossibleMoves(player);

		Transposition t = null;
		short prevAlpha = alpha;
		//= tt.get(inputBoard.getHash());
		
		if (player == 1) {
			t = tt1.get(inputBoard.getHash());
		}
		else {
			t = tt2.get(inputBoard.getHash());
		}
		
		if (t != null) {
			if (t.getDepth() >= depth) {
				if (t.getType() == Transposition.VALUE_LOWER)
					alpha = (short) Math.max(alpha, t.getValue());
				else if (t.getType() == Transposition.VALUE_UPPER)
					beta = (short) Math.min(beta, t.getValue());
				else {
					alpha = t.getValue();
					beta = t.getValue();
				}
			}
			if (alpha >= beta) // cutoff
				return t.getValue();			
		}
		moves = inputBoard.getPossibleMoves(player);
		
		if (t != null) {
			int idx = moves.indexOf(t.getNextBest());
			if (idx != -1 && idx != 0) {
				Collections.swap(moves, idx, 0);
			}
		}

		short best = Short.MIN_VALUE;
		boolean bestFound = false;
		
		Movement nextBest = null;
		
		short value = 0;

		for (Movement m : moves) {
			value = (short) -alphaBeta(inputBoard.getBoardCopyAfterMove(m),
					(short) (depth - 1), (short) (-beta), (short) (-alpha),
					(byte) (3 - player));
			if (value > best) {
				best = value;
				bestFound = true;
				nextBest = m;
			}
			if (best >= beta) {
				break;
			}
			if (best > alpha)
				alpha = best;
		}

		if (bestFound) {
			if (player == 1) {
				tt1.put(new Transposition(best, depth, prevAlpha, beta, nextBest), inputBoard.getHash());
			}
			else {
				tt2.put(new Transposition(best, depth, prevAlpha, beta, nextBest), inputBoard.getHash());
			}
		}
		return best;
	}

	@Override
	public Movement makeMove(Board board) {
		short depth = 0;
		if (id == 1)
			depth = GameConfig.getInstance().getAi1Depth();// 6;
		else
			depth = GameConfig.getInstance().getAi2Depth();

		tt1 = new TranspositionTable();
		tt2 = new TranspositionTable();
		
		this.board = board;

		Movement decision = null;
		short decisionValue = Short.MIN_VALUE;

		/* ***************** Generating possible moves ***************** */

		List<Movement> moves = board.getPossibleMoves(id);

		if ((id == 1 && board.getPlayer1BoardValue() == board
				.getPlayer1Initial()) || firstMoves != 0) {
			if (firstMoves == 0) {
				firstMoves = firstMovesNum - 1;

				statsVisitedNodes = 0;
				statsMovesNum = 1;
			} else {
				--firstMoves;
			}
			List<Movement> moves2 = new ArrayList<Movement>(moves.size());
			for (Movement m : moves) {
				if (!(m.getChain() < 1 || m.getDistance() != 3))
					moves2.add(m);
			}
			moves = moves2;
		} else if ((id == 2 && board.getPlayer2BoardValue() == board
				.getPlayer2Initial()) || firstMoves != 0) {
			if (firstMoves == 0) {
				firstMoves = firstMovesNum - 1;

				statsVisitedNodes = 0;
				statsMovesNum = 1;
			} else {
				--firstMoves;
			}
			List<Movement> moves2 = new ArrayList<Movement>(moves.size());
			for (Movement m : moves) {
				if (!(m.getChain() < 1 || m.getDistance() != 3))
					moves2.add(m);
			}
			moves = moves2;
		}

		byte enemy = (byte) (3 - id);

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
					if (board.getBoardCopyAfterMove(m).hasForwardMoves(enemy)) {
						moves3.add(m);
					}
				}
				if (!moves3.isEmpty()) {
					moves = moves3;
				}
			}
		} else {
			sortEnable = GameConfig.getInstance().isSortEnabled();
			if (sortEnable)
				Collections.sort(moves, new MovementComparator());
		}

		/* ************************************************************* */

		short temp = Short.MIN_VALUE;

		for (int i = 0; i < moves.size(); ++i) {
			System.out.println("->" + i);
			temp = (short) -cStar(board.getBoardCopyAfterMove(moves.get(i)),
					(short) (depth - 1), (byte) (3 - id));
			if (temp > decisionValue) {
				decisionValue = temp;
				decision = moves.get(i);
			}
		}

		if (decision == null) {
			System.err.println("DECISION ERROR!");
			System.exit(-1);
		}
		return decision;
	}
}