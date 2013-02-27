package pushgame.gui.boardpanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import pushgame.logic.Board;
import pushgame.util.GameConfig;
import pushgame.util.GlobalSettings;

/**
 * JPanel able to display game (board) state - drawing board and checkers.
 * @author blacksmithy
 */
public class BoardPanel extends JPanel {

	private static final long serialVersionUID = 5489617923147756922L;
	/**
	 * Board object with actual game (board) state.
	 */
	protected Board board;
	
	/**
	 * Default constructor. Sets default size (512x512) and background color (white).
	 */
	public BoardPanel() {
		super();
		this.setSize(512, 512);
		this.setBackground(Color.WHITE);
		this.board = null;
		this.setVisible(true);
	}
	
	/**
	 * Sets Board object displayed on this panel.
	 * @param board Board object to set.
	 */
	public void setBoard(Board board) {
		this.board = board;
	}
	
	/**
	 * Returns panel's Board object.
	 * @return Board.
	 */
	public Board getBoard() {
		return this.board;
	}
	
	/**
	 * Forces refresh on panel. Updates game state and repaints board.
	 */
	public void refreshState() {
		repaint();
	}
	
	/**
	 * Draws all fields on the board.
	 * @param g Graphics2D object to draw.
	 */
	protected void drawFields(Graphics2D g) {
		Color fieldColor = GlobalSettings.GUI_FIELD_COLOR;
		g.setColor(fieldColor);
		int x =0;
		int y=0; 
		int fieldSize = GlobalSettings.GUI_BOARD_FIELD_SIZE;
				
		for (int i =0; i < 8; ++i) {
			for (int j =0; j < 8; ++j) {
				if (i % 2 == 0 && j % 2 == 0) {
					g.fillRect(fieldSize * j, i * fieldSize, fieldSize, fieldSize);
					g.setColor(Color.black);
					g.drawRect(fieldSize * j, i * fieldSize, fieldSize, fieldSize);
					g.setColor(fieldColor);
				}
				else if (i % 2 == 1 && j % 2 == 1) {
					g.fillRect(fieldSize * j, i * fieldSize, fieldSize, fieldSize);
					g.setColor(Color.black);
					g.drawRect(fieldSize * j, i * fieldSize, fieldSize, fieldSize);
					g.setColor(fieldColor);
				}
			}
		}
		
		g.fillRect(x, y, GlobalSettings.GUI_BOARD_FIELD_SIZE, GlobalSettings.GUI_BOARD_FIELD_SIZE);
	}
	
	
	/**
	 * Draws pawn on the board.
	 * @param g Graphics2D object to draw.
	 * @param color Checker's color.
	 * @param row Checker's position in row.
	 * @param column Checker's position in column.
	 */
	protected void drawPawn(Graphics2D g, Color color, int row, int column) {
		int size = GlobalSettings.GUI_BOARD_FIELD_SIZE;
		int pawnSize = GlobalSettings.GUI_BOARD_PAWN_SIZE;
		int offset = (size - pawnSize) / 2;
		g.setColor(Color.black);
		g.fillOval(column * size + offset, row * size + offset, pawnSize, pawnSize);
		g.setColor(color);
		g.fillOval(column * size + offset + 3, row * size + offset + 3, pawnSize-6, pawnSize-6);
	}
	
	
	/**
	 * Updates (draws) game state on the panel using it's Board object.
	 * @param g Graphics2D object to draw.
	 */
	protected void drawBoardContent(Graphics2D g) {
		if (board == null)
			return;
		byte val = 0;
		for (byte i = 0; i < GlobalSettings.BOARD_SIZE; ++i) {
			for (byte j = 0; j < GlobalSettings.BOARD_SIZE; ++j) {
				if ((val = board.get(i, j)) != 0) {
					if (val == GlobalSettings.PLAYER_1_ID)
						drawPawn(g, GameConfig.player1Color, i, j);
					else
						drawPawn(g, GameConfig.player2Color, i, j);
				}
			}
		}
	}
 	
	/**
	 * Paints component. Calls drawBoardContent() and draws board.
	 */
	protected void paintComponent(Graphics gg) {
	   super.paintComponent(gg);       
	   Graphics2D g = (Graphics2D) gg;
	   g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	   drawFields(g);
	   g.setColor(Color.black);
	   g.drawRect(0, 0, GlobalSettings.GUI_BOARD_PANEL_SIZE-1, GlobalSettings.GUI_BOARD_PANEL_SIZE-1);
	   drawBoardContent(g);
	  
	 }
}
