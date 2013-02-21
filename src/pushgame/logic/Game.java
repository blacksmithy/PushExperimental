package pushgame.logic;

import java.util.Date;

import pushgame.gui.BoardPanel;
import pushgame.gui.GameWindow;
import pushgame.players.Player;

public class Game {
	
	private Player player1;
	private Player player2;
	private BoardPanel boardPanel;
	private GameWindow window;

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
		int i = 30000;
		while ((winner = board.getWinner()) == 0) {
			System.out.println("P1 @ " + new Date());
			start = System.nanoTime();
			move = player1.makeMove(board);
			end = System.nanoTime();
			System.out.println("P1 \"thinking\" time: " + ((end - start)) + "ns");

			board.makeMove(move);
			
			//boardPanel.refreshState();
			//window.refreshBoard();
			window.repaint();
			
			if (board.getWinner() != 0)
				break;
			
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
			System.out.println("P2 @ " + new Date());
			start = System.nanoTime();
			move = player2.makeMove(board);
			end = System.nanoTime();
			System.out.println("P2 \"thinking\" time: " + ((end - start)) + "ns");
			// TODO make move here like board.makeMove(move)
			
			board.makeMove(move);
			
			//boardPanel.refreshState();
			//window.refreshBoard();
			window.repaint();
			
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}


			if (i == 0)
				break;
			--i;
		}
		System.out.println("And the winner is... PLAYER" + winner + "!");
	}

}
