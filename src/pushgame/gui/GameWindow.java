package pushgame.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pushgame.logic.Board;
import pushgame.logic.Game;
import pushgame.players.Player;
import pushgame.players.RandomPlayer;

import java.awt.Color;

public class GameWindow extends JFrame {

	private static final long serialVersionUID = 5300634654382603624L;

	private JPanel contentPane;
	private BoardPanel boardPanel;
	private Refresher r;

	/**
	 * Create the frame.
	 */
	public GameWindow() {
		setTitle("Push: The Game 2.0 | PreAlpha");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(112, 128, 144));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		boardPanel = new BoardPanel();
		boardPanel.setBounds(34, 26, 512, 512);
		contentPane.add(boardPanel);
		
		Board board = new Board();
		board.initialize();
		
//		board.set((byte)3, (byte)3, (byte)1);
//		board.set((byte)4, (byte)3, (byte)1);
//		board.set((byte)5, (byte)3, (byte)1);
//		board.set((byte)6, (byte)3, (byte)1);
		

		
		
		boardPanel.setBoard(board);
		boardPanel.repaint();
		
		//r = new Refresher(this);
		//r.start();
		//r.run();
		
		//System.out.println("MOVES = " + board.getPossibleMoves((byte) 2).size());
	}
	
	public void refreshBoard() {
		this.revalidate();
		this.repaint();
		boardPanel.refreshState();
		boardPanel.repaint();
	};
	
	public void playTheGame() {
		Player player1 = new RandomPlayer((byte) 1, 100);
		Player player2 = new RandomPlayer((byte) 2, 100);
		//Refresher r = new Refresher(this);
		//r.run();
		Game game = new Game(player1, player2, this);
		game.play();
		//System.out.println("MOVES1 = " + boardPanel.getBoard().getPossibleMoves((byte) 1).size());
		//System.out.println("MOVES2 = " + boardPanel.getBoard().getPossibleMoves((byte) 2).size());
	}
	
	public BoardPanel getBoardPanel() {
		return this.boardPanel;
	}

}

class Refresher extends Thread {
	private GameWindow window;
	public Refresher(GameWindow window) {
		super();
		this.window = window;
	}
	
	public void run() {
		while (true) {
			window.repaint();
			window.refreshBoard();
			window.repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
