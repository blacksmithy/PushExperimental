package pushgame.gui.windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import pushgame.gui.boardpanel.InteractiveBoardPanel;
import pushgame.logic.Board;
import pushgame.logic.Game;
import pushgame.players.BoardEditor;
import pushgame.players.Player;
import pushgame.players.PlayerFactory;
import pushgame.util.GameConfig;


@SuppressWarnings("serial")
public class GameWindow extends JFrame implements ActionListener, WindowListener, Runnable
{
	static public GameWindow curr;
	boolean editMode=false;
	InteractiveBoardPanel checker;
	Game game;
	Point start;
	public JLabel lblGracz_1 = new JLabel("Gracz 1");
	public JLabel lblGracz_2 = new JLabel("Gracz 2");
	public static Font IDLE_FONT=new Font("Courier New", Font.PLAIN, 16);
	public static Font ACTIVE_FONT=new Font("Arial Black", Font.BOLD, 20);
	JButton pause = new JButton("Wstrzymaj");
	JButton savegame = new JButton("Zapisz");
	JButton exit = new JButton("Wyjdź");
	public JProgressBar time1 = new JProgressBar(SwingConstants.VERTICAL, 0, 0);
	public JProgressBar time2 = new JProgressBar(SwingConstants.VERTICAL, 0, 0);
	public JLabel timer[] = { new JLabel("Czas"), new JLabel("0:00"),
			new JLabel("Czas"), new JLabel("0:00") };

	public static GameWindow startNewWindow()
	{
		GameWindow win=new GameWindow();
		Thread winThr=new Thread(win);
		winThr.start();
		return win;
	}	
	
	public static GameWindow launchExistingGame(String fname) throws FileNotFoundException, ClassNotFoundException, IOException
	{
		InteractiveBoardPanel panel=new InteractiveBoardPanel();
		panel.loadFromFile(fname);
		GameWindow win=new GameWindow(panel);
		Thread winThr=new Thread(win);
		winThr.start();
		return win;
	}	
	
	public static GameWindow launchEditMode()
	{
		GameWindow win=new GameWindow(true);
		Thread winThr=new Thread(win);
		winThr.start();
		return win;
	}
	
	public void run()
	{
		playTheGame();
	}

	public GameWindow()
	{
		super("Gra");
		// setMinimumSize(minimumSize)
		setBounds(10, 10, 700, 600);
		setBackground(Color.DARK_GRAY);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setEnabled(true);
		setVisible(true);
		addWindowListener(this);
		getContentPane().setLayout(null);
		start = new Point(20, 30);
		checker = new InteractiveBoardPanel();

		pause.setBounds(570, 180, 100, 40);
		savegame.setBounds(570, 250, 100, 40);
		exit.setBounds(570, 320, 100, 40);
		pause.addActionListener(this);
		exit.addActionListener(this);
		savegame.addActionListener(this);
		getContentPane().add(pause);
		getContentPane().add(exit);
		getContentPane().add(savegame);

		time1.setBounds(570, 30, 20, 120);
		time1.setForeground(GameConfig.player1Color);
		time1.setMaximum(1);
		time1.setValue(1);
		getContentPane().add(time1);
		timer[0].setBounds(600, 50, 80, 50);
		timer[0].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		getContentPane().add(timer[0]);
		timer[1].setBounds(600, 90, 80, 50);
		timer[1].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		getContentPane().add(timer[1]);

		time2.setBounds(570, 400, 20, 120);
		time2.setForeground(GameConfig.player2Color);
		time2.setMaximum(1);
		time2.setValue(1);
		getContentPane().add(time2);
		timer[2].setBounds(600, 430, 80, 50);
		timer[2].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		getContentPane().add(timer[2]);
		timer[3].setBounds(600, 470, 80, 50);
		timer[3].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		getContentPane().add(timer[3]);

		
		//pass.setEnabled(false);
		// new Finisher(this).start();
		//checker.game.setupRefreshTarget(this);
		Board beg=new Board();
		beg.initialize();
		checker.setBoard(beg);
		checker.setVisible(true);
		checker.setLocation(start);
		getContentPane().add(checker);
		
		lblGracz_1.setBounds(595, 30, 94, 30);
		lblGracz_1.setFont(ACTIVE_FONT);
		lblGracz_1.setVisible(true);
		getContentPane().add(lblGracz_1);
		
		lblGracz_2.setBounds(595, 400, 94, 30);
		lblGracz_2.setFont(ACTIVE_FONT);
		lblGracz_2.setVisible(true);
		getContentPane().add(lblGracz_2);
		//playTheGame();
	}
	
	public GameWindow(InteractiveBoardPanel panel)
	{
		super("Gra");
		// setMinimumSize(minimumSize)
		setBounds(10, 10, 700, 600);
		setBackground(Color.GRAY);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setEnabled(true);
		setVisible(true);
		addWindowListener(this);
		getContentPane().setLayout(null);
		start = new Point(20, 50);
		checker = panel;

		pause.setBounds(570, 180, 100, 40);
		savegame.setBounds(570, 250, 100, 40);
		exit.setBounds(570, 320, 100, 40);
		pause.addActionListener(this);
		exit.addActionListener(this);
		savegame.addActionListener(this);
		getContentPane().add(pause);
		getContentPane().add(exit);
		getContentPane().add(savegame);

		time1.setBounds(570, 30, 20, 120);
		time1.setForeground(GameConfig.player1Color);
		time1.setMaximum(1);
		time1.setValue(1);
		getContentPane().add(time1);
		timer[0].setBounds(600, 50, 80, 50);
		timer[0].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		getContentPane().add(timer[0]);
		timer[1].setBounds(600, 90, 80, 50);
		timer[1].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		getContentPane().add(timer[1]);

		time2.setBounds(570, 400, 20, 120);
		time2.setForeground(GameConfig.player2Color);
		time2.setMaximum(1);
		time2.setValue(1);
		getContentPane().add(time2);
		timer[2].setBounds(600, 430, 80, 50);
		timer[2].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		getContentPane().add(timer[2]);
		timer[3].setBounds(600, 470, 80, 50);
		timer[3].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		getContentPane().add(timer[3]);

		
		checker.setVisible(true);
		checker.setLocation(start);
		getContentPane().add(checker);
		
		lblGracz_1.setBounds(595, 30, 94, 30);
		lblGracz_1.setFont(ACTIVE_FONT);
		lblGracz_1.setVisible(true);
		getContentPane().add(lblGracz_1);
		
		lblGracz_2.setBounds(595, 400, 94, 30);
		lblGracz_2.setFont(ACTIVE_FONT);
		lblGracz_2.setVisible(true);
		getContentPane().add(lblGracz_2);
		//playTheGame();
	}
	
	public void playTheGame() {
		Player player1;
		Player player2;
		if(editMode)
		{
			player1 = new BoardEditor();
			player2 = new BoardEditor();
		}
		else
		{
			player1 = PlayerFactory.getPlayer((byte) 1, GameConfig.delay);
			player2 = PlayerFactory.getPlayer((byte) 2, GameConfig.delay);
		}
		game = new Game(player1, player2, this);
		if(editMode) {game.hold();}
		game.play();
		checker.repaint();
		checker.refreshState();
	}
	
	public void refreshBoard() {
		checker.getParent().repaint();
		this.revalidate();
		this.repaint();
		checker.refreshState();
		checker.repaint();
	};
	
	
	public InteractiveBoardPanel getBoardPanel() {
		return this.checker;
	}
	
	
	public GameWindow(boolean editable)
	{
		this();
		editMode=editable;
		pause.setEnabled(false);
	}
	
	
	public void onExit()
	{
		game.endGame();
	}

	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		if (src == pause)
		{
			if(pause.getText()=="Wstrzymaj") {
				game.hold();
				checker.setVisible(false);
				pause.setText("Wznów");
			}
			else if(pause.getText()=="Wznów") {
				game.unhold();
				checker.setVisible(true);
				pause.setText("Wstrzymaj");
			}
		}
		else if (src == exit)
		{
			onExit();
			this.dispose();
		}
		else if (src == savegame)
		{
			game.hold();
			
			JFileChooser fs=new JFileChooser();
			int returnVal = fs.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fs.getSelectedFile();
	            System.out.println(file.getPath());
	            try
				{
					checker.saveToFile(file.getPath());
				}
				catch (IOException e1)
				{
					JOptionPane.showMessageDialog(null,"Nie udało się zapisać gry.");
				}
	        }
	        
	        if(!editMode)
	        	{game.unhold();}
		}
		repaint();
	}


	@Override
	public void windowClosing(WindowEvent e)
	{
		onExit();
	}

	public void windowClosed(WindowEvent e){}
	public void windowActivated(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
}
