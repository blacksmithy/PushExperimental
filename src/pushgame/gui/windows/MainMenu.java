package pushgame.gui.windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class MainMenu extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 637053531039442490L;
	JButton startGame = new JButton("Nowa gra");
	JButton loadGame = new JButton("Załaduj grę");
	JButton editBoard = new JButton("Ustaw planszę");
	JButton players = new JButton("Wybór graczy");
	JButton settings = new JButton("Ustawienia");
	JButton exit = new JButton("Wyjdź");
	JLabel title = new JLabel("PUSH");

	public MainMenu()
	{
		super("Menu");
		setBounds(200, 200, 275, 229);
		setBackground(new Color(51, 179, 255));
		setForeground(new Color(51, 179, 255));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setEnabled(true);
		setVisible(true);
		
		startGame.setLocation(10, 71);
		startGame.setSize(120, 30);
		startGame.addActionListener(this);
		getContentPane().add(startGame);
		
		loadGame.setLocation(140, 71);
		loadGame.setSize(120, 30);
		loadGame.addActionListener(this);
		getContentPane().add(loadGame);
		
		editBoard.setLocation(10, 112);
		editBoard.setSize(120, 30);
		editBoard.addActionListener(this);
		getContentPane().add(editBoard);
		
		players.setLocation(140, 112);
		players.setSize(120, 30);
		players.addActionListener(this);
		getContentPane().add(players);
		
		settings.setLocation(10, 153);
		settings.setSize(120, 30);
		settings.addActionListener(this);
		getContentPane().add(settings);
		
		exit.setLocation(140, 153);
		exit.setSize(120, 30);
		exit.addActionListener(this);
		getContentPane().add(exit);
		
		title.setBounds(68, 10, 138, 50);
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
		getContentPane().add(title);

		getContentPane().setLayout(null);
	}

	public void actionPerformed(ActionEvent e)
	{
	Object src = e.getSource();
		if (src == startGame)
		{
			GameWindow.startNewWindow();
		}
		else if (src == editBoard)
		{
			GameWindow.launchEditMode();
		}
		else if (src == loadGame)
		{
			JFileChooser fs=new JFileChooser();
			int returnVal = fs.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fs.getSelectedFile();
	            try
				{
	            	GameWindow.launchExistingGame(file.getPath());
				}
				catch (ClassNotFoundException | IOException e1)
				{
					JOptionPane.showMessageDialog(null,"Nie uda�o si� otworzy� gry.");
				}
	        }
		}
		else if (src == settings)
		{
			new SettingsWindow();
		}
		else if (src == exit)
		{
			this.dispose();
		}
		else if (src == players)
		{
			new PlayerWindow();
		}
	}

}

class MenuLayout
{
	public int sx=15,sy=70;
	public int w=120,h=30,d=15;
	public int lx=15,ly=70;
	public void setBounds(JButton b)
	{
		b.setBounds(lx, ly, w, h);
		ly+=h+d;
	}
	public void setSize(JFrame f)
	{
		f.setSize(sx+w+30, ly+h);
	}
}
