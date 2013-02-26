package pushgame.logic;

import java.util.Date;
import javax.swing.JOptionPane;


import pushgame.gui.windows.Counter;
import pushgame.gui.windows.GameWindow;
import pushgame.gui.boardpanel.InteractiveBoardPanel;
import pushgame.players.Player;

public class Game {
	
	private Player player1;
	private Player player2;
	private InteractiveBoardPanel boardPanel;
	private GameWindow window;
	//private static final long PLAYER_SLEEP = 0L;
	private boolean active=true;
	private Counter counter;

	public Game(Player player1, Player player2, GameWindow gameWindow) { // BoardPanel boardPanel
		this.player1 = player1;
		this.player2 = player2;
		this.window = gameWindow;
		this.boardPanel = window.getBoardPanel();
		counter=new Counter(gameWindow);
		window.timer[1].setText(counter.getTimeString(1));
		window.timer[3].setText(counter.getTimeString(2));
	}
	
	public void endGame()
	{
		counter.finish();
		active=false;
		boardPanel.quit();
	}
	
	public void hold()
	{
		counter.pause();
	}
	
	public void unhold()
	{
		counter.unpause();
	}
	
	public void play() {
		if (boardPanel.getBoard() == null)
			return;
		Board board = boardPanel.getBoard();
		Movement move = null;
		long start, end;
		byte winner = 0;
		counter.reset();
		counter.start();
		while ((winner = board.getWinner()) == 0) {
			if(!active) {return;}
			System.out.println("P1 @ " + new Date());
			start = System.nanoTime();
			
			counter.setTurn(1);
			boardPanel.setFieldListener(player1);
			move = player1.makeMove(board);
			boardPanel.clearFieldListener();
			if(!active) {return;}
			
			end = System.nanoTime();
			System.out.println("P1 \"thinking\" time: " + ((end - start)) + "ns");
			

			board.makeMove(move);
			boardPanel.getParent().repaint();
			
			window.refreshBoard();
			
			if ((winner = board.getWinner()) != 0)
				break;
			
			try {
				Thread.sleep(player1.getDelay());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("P2 @ " + new Date());
			start = System.nanoTime();
			if(!active) {return;}
			
			counter.setTurn(2);
			boardPanel.setFieldListener(player2);
			move = player2.makeMove(board);
			boardPanel.clearFieldListener();
			if(!active) {return;}
			
			end = System.nanoTime();
			System.out.println("P2 \"thinking\" time: " + ((end - start)) + "ns");
			
			
			board.makeMove(move);
			window.refreshBoard();
			
			try {
				Thread.sleep(player2.getDelay());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!active) {return;}
		}
		System.out.println("P1 nodes(" + player1.getStatsVisitedNodes() + ") \t moves(" + player1.getStatsMovesNum() + ") \t ratio(" + (player1.getStatsVisitedNodes()/player1.getStatsMovesNum()) + ")");
		System.out.println("P2 nodes(" + player2.getStatsVisitedNodes() + ") \t moves(" + player2.getStatsMovesNum() + ") \t ratio(" + (player2.getStatsVisitedNodes()/player2.getStatsMovesNum()) + ")");
		
		endGame();
		System.out.println("And the winner is... PLAYER" + winner + "!");
		JOptionPane.showMessageDialog(null,"Gracz "+Integer.toString(winner)+" wygrał!!!");
	}
}
