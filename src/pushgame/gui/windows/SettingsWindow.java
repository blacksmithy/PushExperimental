package pushgame.gui.windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import pushgame.util.GameConfig;



@SuppressWarnings("serial")
public class SettingsWindow extends JFrame implements ActionListener
{
	JLabel label3 = new JLabel("czas oczekiwania");
	DelaySelection combo3 = new DelaySelection();
	JButton exit = new JButton("zamknij");

	public SettingsWindow()
	{
		super("Opcje");
		// setMinimumSize(minimumSize)
		setBounds(160, 160, 190, 200);
		setBackground(Color.GRAY);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setEnabled(true);
		setVisible(true);

		label3.setBounds(10, 90, 130, 30);
		getContentPane().add(label3);
		combo3.setBounds(120, 90, 50, 30);
		combo3.addActionListener(this);
		getContentPane().add(combo3);

		exit.setBounds(10, 130, 160, 30);
		exit.addActionListener(this);
		getContentPane().add(exit);

		load();
		getContentPane().setLayout(null);
	}

	void load()
	{
		combo3.setSelectedItem((double)GameConfig.delay/1000);
	}

	public void paint(Graphics g)
	{
		super.paint(g);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		if (src == exit)
		{
			GameConfig.delay= (int) ((double)combo3.getSelectedItem()*1000);
			this.dispose();
		}
		repaint();
	}
}

@SuppressWarnings("serial")
class SizeSelection extends JComboBox<Integer>
{
	SizeSelection()
	{
		super();
		addItem(6);
		addItem(8);
		addItem(10);
		addItem(12);
	}
}

@SuppressWarnings("serial")
class DelaySelection extends JComboBox<Double>
{
	DelaySelection()
	{
		super();
		addItem(0.0);
		addItem(0.10);
		addItem(0.25);
		addItem(0.5);
		addItem(0.75);
		addItem(1.0);
	}
}

@SuppressWarnings("serial")
class RowsSelection extends JComboBox<Integer>
{
	RowsSelection()
	{
		super();
		addItem(1);
		addItem(2);
		addItem(3);
		addItem(4);
	}
}