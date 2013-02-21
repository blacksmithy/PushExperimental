package pushgame.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pushgame.logic.Board;

import java.awt.Color;

public class GameWindow extends JFrame {

	private static final long serialVersionUID = 5300634654382603624L;

	private JPanel contentPane;
	private BoardPanel boardPanel;

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
		
		boardPanel.setBoard(board);
		boardPanel.repaint();
	}
	
	public void refreshBoard() {
		boardPanel.getParent().repaint();
		this.revalidate();
		this.repaint();
		boardPanel.refreshState();
		boardPanel.repaint();
		
	};
	
	public BoardPanel getBoardPanel() {
		return this.boardPanel;
	}

}