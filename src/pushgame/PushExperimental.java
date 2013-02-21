package pushgame;

import java.awt.EventQueue;

import pushgame.gui.GameWindow;
import pushgame.logic.Game;
import pushgame.players.Player;
import pushgame.players.RandomPlayer;


public class PushExperimental {

	static private GameWindow frame;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new GameWindow();
					frame.setVisible(true);
					playTheGame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	static public void playTheGame() {
		Player player1 = new RandomPlayer((byte) 1, 100);
		Player player2 = new RandomPlayer((byte) 2, 100);
		//Refresher r = new Refresher(this);
		//r.run();
		Game game = new Game(player1, player2, frame);
		game.play();
		//System.out.println("MOVES1 = " + boardPanel.getBoard().getPossibleMoves((byte) 1).size());
		//System.out.println("MOVES2 = " + boardPanel.getBoard().getPossibleMoves((byte) 2).size());
	}
}
