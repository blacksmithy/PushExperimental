package pushgame;

import java.awt.EventQueue;

import pushgame.gui.GameWindow;
import pushgame.logic.Game;
import pushgame.players.HumanPlayer;
import pushgame.players.Player;
import pushgame.players.RandomPlayer;


public class PushExperimental {

	static private GameWindow frame;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		frame = new GameWindow();
		frame.setVisible(true);
		frame.repaint();
		playTheGame();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	static public void playTheGame() {
		Player player1 = new RandomPlayer((byte) 1, 100);
		Player player2 = new HumanPlayer((byte) 2, 0);
		Game game = new Game(player1, player2, frame);
		game.play();
	}
}
