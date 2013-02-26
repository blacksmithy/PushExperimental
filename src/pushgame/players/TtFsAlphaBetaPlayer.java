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
		
		TranspositionTable tt = new TranspositionTable();
		
		Movement decision = null;
		short decisionValue = Short.MIN_VALUE;
		short alpha = Short.MIN_VALUE + 2;
		short beta = Short.MAX_VALUE - 1;
		
		/* ***************** Generating possible moves ***************** */
		List<Movement> moves = board.getPossibleMoves(id);
		
		if ((id == 1 && board.getPlayer1BoardValue() == board.getPlayer1Initial()) || firstMoves != 0) {
			if (firstMoves == 0) {
				firstMoves = firstMovesNum - 1;
			}
			else {
				--firstMoves;
			}
			List<Movement> moves2 = new ArrayList<Movement>(moves.size());
			for (Movement m : moves) {
				if (!(m.getChain() < 1 || m.getDistance() != 3))
					moves2.add(m);
			}
			moves = moves2;
		}
		else if ((id == 2 && board.getPlayer2BoardValue() == board.getPlayer2Initial()) || firstMoves != 0) {
			if (firstMoves == 0) {
				firstMoves = firstMovesNum - 1;
			}
			else {
				--firstMoves;
			}
			List<Movement> moves2 = new ArrayList<Movement>(moves.size());
			for (Movement m : moves) {
				if (!(m.getChain() < 1 || m.getDistance() != 3))
					moves2.add(m);
			}
			moves = moves2;
		}
		
		sortEnable = GameConfig.getInstance().isSortEnabled();
		if (sortEnable)
			Collections.sort(moves, new MovementComparator());
		/* ************************************************************* */
		
		int iterStep = 2;
		if (forceOneThread) {
			iterStep = 1;
		}
		
		for (int i = 0; i < moves.size(); i += iterStep) {
			System.out.println("->" + i);
			
			threads[0] = new TtFsAlphaBetaThread((byte) 0, oracle, board.getBoardCopyAfterMove(moves.get(i)), this, tt, (short) (depth - 1), (short) (-beta), (short) (-alpha), (byte) (3 - id));
			threads[0].run();
			if (! forceOneThread && (i + 1 < moves.size())) {
				threads[1] = new TtFsAlphaBetaThread((byte) 1, oracle2, board.getBoardCopyAfterMove(moves.get(i+1)), this, tt, (short) (depth - 1), (short) (-beta), (short) (-alpha), (byte) (3 - id));
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

class TtFsAlphaBetaThread extends Thread {
	private byte threadId;
	private Oracle oracle;
	private Board board;
	private AlphaBetaThreadEndEvent event;
	private TranspositionTable tt;
	private short depth;
	private short alpha;
	private short beta;
	private byte player;
	
	public TtFsAlphaBetaThread(byte threadId, Oracle oracle, Board board,
			AlphaBetaThreadEndEvent event, TranspositionTable tt, short depth, short alpha, short beta,
			byte player) {
		super();
		this.threadId = threadId;
		this.oracle = oracle;
		this.board = board;
		this.event = event;
		this.tt = tt;
		this.depth = depth;
		this.alpha = alpha;
		this.beta = beta;
		this.player = player;
	}
	
	private short alphaBeta(Board inputBoard, short depth, short alpha, short beta, byte player) {
		
		if ((depth == 0) || inputBoard.getWinner() != 0) {
			return this.oracle.getProphecy(this.board, inputBoard, null, player);
		}
		
		List<Movement> moves; // = inputBoard.getPossibleMoves(player);
		
		Transposition t = tt.get(inputBoard.getHash());
		if (t != null) { // jeśli znaleziono coś w tablicy transpozycji
			//System.out.println("HIT!");
			
			if (t.getDepth() >= depth) { // i wynik może mieć znaczenie na tym poziomie
				if (t.getType() == Transposition.VALUE_LOWER)
					alpha = (short) Math.max(alpha, t.getValue());
				else if (t.getType() == Transposition.VALUE_UPPER)
					beta = (short) Math.min(beta, t.getValue());
				else {
					alpha = t.getValue();
					beta = t.getValue();
				}
			}
			if (alpha >= beta) // odcięcie
				return t.getValue();
			
			moves = inputBoard.getPossibleMoves(player);
			
			int idx = moves.indexOf(t.getNextBest());
			if (idx != -1 && idx != 0) {
				Collections.swap(moves, idx, 0);
			}
			//moves.add(t.getNextBest());
		}
		else {
			moves = inputBoard.getPossibleMoves(player);
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
			//System.out.println("HASH = " + inputBoard.getHash());
			tt.put(new Transposition(best, depth, alpha, beta, nextBest), inputBoard.getHash());
			//System.out.println("TT_SIZE = " + tt.getSize());
		}
		return best;
	}
	
	public void run() {
		short value = (short) -alphaBeta(board, depth, alpha, beta, player);
		event.onThreadEnd(threadId, value);
	}
	
}
