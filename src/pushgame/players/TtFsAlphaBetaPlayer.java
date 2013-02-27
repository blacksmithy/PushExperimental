package pushgame.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pushgame.logic.Board;
import pushgame.logic.Movement;
import pushgame.oracle.Oracle;
import pushgame.oracle.SymmetricDistancesOracle;
import pushgame.util.AlphaBetaThreadEndEvent;
import pushgame.util.GameConfig;
import pushgame.util.MovementComparator;
import pushgame.util.Transposition;
import pushgame.util.TranspositionTable;

public class TtFsAlphaBetaPlayer extends Player implements AlphaBetaThreadEndEvent {

	private Oracle oracle;
	private Oracle oracle2;
	private short[] threadReturn;
	private TtFsAlphaBetaThread[] threads;
	private boolean forceOneThread = true;	
	
	private int firstMoves = 0;
	private int firstMovesNum = 2;
	private boolean sortEnable = true;
	
	public TtFsAlphaBetaPlayer(byte id, int delay) {
		super(id, delay);
		oracle = new SymmetricDistancesOracle();
		oracle2 = new SymmetricDistancesOracle();
		threadReturn = new short[2];
		threads = new TtFsAlphaBetaThread[2];
	}

	@Override
	public Movement makeMove(Board board) {
		short depth = 0;
		if (id == 1)
			depth = GameConfig.getInstance().getAi1Depth();//6;
		else
			depth = GameConfig.getInstance().getAi2Depth();
		
		TranspositionTable tt1 = new TranspositionTable();
		TranspositionTable tt2 = new TranspositionTable();
		
		Movement decision = null;
		short decisionValue = Short.MIN_VALUE;
		short alpha = Short.MIN_VALUE + 2;
		short beta = Short.MAX_VALUE - 1;
		
		++statsMovesNum;
		
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
		
		int iterStep = 2;
		if (forceOneThread) {
			iterStep = 1;
		}
		
		for (int i = 0; i < moves.size(); i += iterStep) {
			System.out.println("->" + i);
			
			threads[0] = new TtFsAlphaBetaThread((byte) 0, oracle, board.getBoardCopyAfterMove(moves.get(i)), this, tt1, tt2, (short) (depth - 1), (short) (-beta), (short) (-alpha), (byte) (3 - id));
			threads[0].run();
			if (! forceOneThread && (i + 1 < moves.size())) {
				threads[1] = new TtFsAlphaBetaThread((byte) 1, oracle2, board.getBoardCopyAfterMove(moves.get(i+1)), this, tt1, tt2, (short) (depth - 1), (short) (-beta), (short) (-alpha), (byte) (3 - id));
				threads[1].run();
			}
			
			try {
				threads[0].join();
				if (! forceOneThread && (i + 1 < moves.size())) {
					threads[1].join();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (threadReturn[0] > alpha) {
				alpha = threadReturn[0];
			}
			if (alpha >= beta) {
			//	System.out.println("ODCIĘCIE!!!!!!!!!");
				break;
			}
			if (threadReturn[0] > decisionValue)
			{
				decisionValue = threadReturn[0];
				decision = moves.get(i);
			}
			if (! forceOneThread && (i + 1 < moves.size())) {
				if (threadReturn[1] > alpha) {
					alpha = threadReturn[1];
				}
				if (alpha >= beta) {
					break;
				}
				if (threadReturn[1] > decisionValue) {
					decisionValue = threadReturn[1];
					decision = moves.get(i+1);
				}
			}
			
			if (decision == null) {
				System.err.println("DECISION ERROR!");
				System.exit(-1);
			}

		}
		
		System.out.println("TT:" + tt1.getSize() + tt2.getSize());

		return decision;
	}

	@Override
	public void onThreadEnd(byte threadId, short value) {
		threadReturn[threadId] = value;		
	}
	
	@Override
	public void onThreadEndCountNodes(long nodes) {
		this.statsVisitedNodes += nodes;		
	}

}

class TtFsAlphaBetaThread extends Thread {
	private byte threadId;
	private Oracle oracle;
	private Board board;
	private AlphaBetaThreadEndEvent event;
	private TranspositionTable tt1;
	private TranspositionTable tt2;
	private short depth;
	private short alpha;
	private short beta;
	private byte player;
	
	private long nodesVisited;
	
	public TtFsAlphaBetaThread(byte threadId, Oracle oracle, Board board,
			AlphaBetaThreadEndEvent event, TranspositionTable tt1, TranspositionTable tt2, short depth, short alpha, short beta,
			byte player) {
		super();
		this.threadId = threadId;
		this.oracle = oracle;
		this.board = board;
		this.event = event;
		this.tt1 = tt1;
		this.tt2 = tt2;
		this.depth = depth;
		this.alpha = alpha;
		this.beta = beta;
		this.player = player;
		
		this.nodesVisited = 0;
	}
	
	private short alphaBeta(Board inputBoard, short depth, short alpha, short beta, byte player) {
		
		++nodesVisited;
		
		if ((depth == 0) || inputBoard.getWinner() != 0) {
			return this.oracle.getProphecy(this.board, inputBoard, null, player);
		}
		
		List<Movement> moves = null;
		Transposition t = null;
		short prevAlpha = alpha;

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
			value = (short) -alphaBeta(inputBoard.getBoardCopyAfterMove(m), (short) (depth-1), (short) (-beta), (short) (-alpha), (byte) (3 - player));
			if (value > best) {
				best = value;
				nextBest = m;
				bestFound = true;
			}
			if (best >= beta) {
				break;
			}
			if (best > alpha)
				alpha = best;
		}
		
		if (bestFound) {
			if (player == 1) {
				//if (t == null || depth < t.getDepth() || (depth == t.getDepth() && best > t.getValue()))
					tt1.put(new Transposition(best, depth, prevAlpha, beta, nextBest), inputBoard.getHash());
			}
			else {
				//if (t == null || depth < t.getDepth() || (depth == t.getDepth() && best > t.getValue()))
					tt2.put(new Transposition(best, depth, prevAlpha, beta, nextBest), inputBoard.getHash());
			}
		}
		return best;
	}
	
	public void run() {
		short value = (short) -alphaBeta(board, depth, alpha, beta, player);
		event.onThreadEnd(threadId, value);
		event.onThreadEndCountNodes(this.nodesVisited);
	}
	
}
