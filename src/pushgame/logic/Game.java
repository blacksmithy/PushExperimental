package pushgame.logic;

import java.util.Date;

import pushgame.gui.GameWindow;
import pushgame.gui.InteractiveBoardPanel;
import pushgame.players.Player;

public class Game {
	
	private Player player1;
	private Player player2;
	private InteractiveBoardPanel boardPanel;
	private GameWindow window;
	private static final long PLAYER_SLEEP = 0L;

	public Game(Player player1, Player player2, GameWindow gameWindow) { // BoardPanel boardPanel
		this.player1 = player1;
		this.player2 = player2;
		this.window = gameWindow;
		this.boardPanel = window.getBoardPanel();
	}
	
	public void play() {
		if (boardPanel.getBoard() == null)
			return;
		Board board = boardPanel.getBoard();
		Movement move = null;
		long start, end;
		byte winner = 0;
		while ((winner = board.getWinner()) == 0) {
			System.out.println("P1 @ " + new Date());
			start = System.nanoTime();
			
			boardPanel.setFieldListener(player1);
			move = player1.makeMove(board);
			boardPanel.clearFieldListener();
			
			end = System.nanoTime();
			System.out.println("P1 \"thinking\" time: " + ((end - start)) + "ns");

			board.makeMove(move);
			boardPanel.getParent().repaint();
			
			window.refreshBoard();
			
			if (board.getWinner() != 0)
				break;
			
			try {
				Thread.sleep(PLAYER_SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("P2 @ " + new Date());
			start = System.nanoTime();
			
			boardPanel.setFieldListener(player2);
			move = player2.makeMove(board);
			boardPanel.clearFieldListener();
			
			end = System.nanoTime();
			System.out.println("P2 \"thinking\" time: " + ((end - start)) + "ns");
			
			board.makeMove(move);
			
			window.refreshBoard();
			
			try {
				Thread.sleep(PLAYER_SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("And the winner is... PLAYER" + winner + "!");
	}

}
