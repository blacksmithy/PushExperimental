package pushgame.players;

import java.util.List;

import pushgame.logic.Board;
import pushgame.logic.Movement;
import pushgame.oracle.Oracle;
import pushgame.oracle.SymmetricDistancesOracle;
import pushgame.util.AlphaBetaThreadEndEvent;
import pushgame.util.GameConfig;

public class FsAlphaBetaPlayer extends Player implements AlphaBetaThreadEndEvent {

	private Oracle oracle;
	private Oracle oracle2;
	private short[] threadReturn;
	private FsAlphaBetaThread[] threads;
	private boolean forceOneThread = true;
	
	public FsAlphaBetaPlayer(byte id, int delay) {
		super(id, delay);
		oracle = new SymmetricDistancesOracle();
		oracle2 = new SymmetricDistancesOracle();
		threadReturn = new short[2];
		threads = new FsAlphaBetaThread[2];
	}

	@Override
	public Movement makeMove(Board board) {
		short depth = 0;
		if (id == 1)
			depth = GameConfig.getInstance().getAi1Depth();//6;
		else
			depth = GameConfig.getInstance().getAi2Depth();
		
		Movement decision = null;
		short decisionValue = Short.MIN_VALUE;
		short alpha = Short.MIN_VALUE + 2;
		short beta = Short.MAX_VALUE - 1;
		
		List<Movement> moves = board.getPossibleMoves(id);
		
		int iterStep = 2;
		if (forceOneThread) {
			iterStep = 1;
		}
		
		for (int i = 0; i < moves.size(); i += iterStep) {
			System.out.println("->" + i);
			
			threads[0] = new FsAlphaBetaThread((byte) 0, oracle, board.getBoardCopyAfterMove(moves.get(i)), this, (short) (depth - 1), (short) (-beta), (short) (-alpha), (byte) (3 - id));
			threads[0].run();
			if (! forceOneThread && (i + 1 < moves.size())) {
				threads[1] = new FsAlphaBetaThread((byte) 1, oracle2, board.getBoardCopyAfterMove(moves.get(i+1)), this, (short) (depth - 1), (short) (-beta), (short) (-alpha), (byte) (3 - id));
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
					//System.out.println("i: " + i + " NOWA ALFA: " + alpha);
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
						//System.out.println("i: " + i + " NOWA ALFA: " + alpha);
				}
				if (alpha >= beta) {
					//System.out.println("ODCIĘCIE!!!!!!!!!");
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

		return decision;
	}

	@Override
	public void onThreadEnd(byte threadId, short value) {
		threadReturn[threadId] = value;		
	}

}

class FsAlphaBetaThread extends Thread {
	private byte threadId;
	private Oracle oracle;
	private Board board;
	private AlphaBetaThreadEndEvent event;
	private short depth;
	private short alpha;
	private short beta;
	private byte player;
	
	public FsAlphaBetaThread(byte threadId, Oracle oracle, Board board,
			AlphaBetaThreadEndEvent event, short depth, short alpha, short beta,
			byte player) {
		super();
		this.threadId = threadId;
		this.oracle = oracle;
		this.board = board;
		this.event = event;
		this.depth = depth;
		this.alpha = alpha;
		this.beta = beta;
		this.player = player;
	}
	
	private short alphaBeta(Board inputBoard, short depth, short alpha, short beta, byte player) {
		
		if ((depth == 0) || inputBoard.getWinner() != 0) {
			return this.oracle.getProphecy(this.board, inputBoard, null, player);
		}
		
		short best = Short.MIN_VALUE;
		
		List<Movement> moves = inputBoard.getPossibleMoves(player);
		
		short value = 0;
		
		for (Movement m : moves) {
			value = (short) -alphaBeta(inputBoard.getBoardCopyAfterMove(m), (short) (depth-1), (short) (-beta), (short) (-alpha), (byte) (3 - player));
			if (value > best)
				best = value;
			if (best >= beta) {
				break;
			}
			if (best > alpha)
				alpha = best;
		}
		return best;
	}
	
	public void run() {
		short value = (short) -alphaBeta(board, depth, alpha, beta, player);
		event.onThreadEnd(threadId, value);
	}
	
}
