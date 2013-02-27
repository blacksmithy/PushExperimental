package pushgame.gui.windows;


import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JTextPane;



@SuppressWarnings("serial")
public class SettingsWindow extends JFrame
{

	public SettingsWindow()
	{
		super("Pomoc");
		setSize(500,500);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JTextPane pane1 = new JTextPane();
		pane1.setEditable(false);
		tabbedPane.addTab("Autorzy", pane1);

		
		JTextPane pane2 = new JTextPane();
		pane1.setEditable(false);
		tabbedPane.addTab("Zasady gry", pane2);
		
		JTextPane pane3 = new JTextPane();
		pane1.setEditable(false);
		tabbedPane.addTab("Korzystanie z aplikacji", pane3);
		

		setEnabled(true);
		setVisible(true);
	}
}
