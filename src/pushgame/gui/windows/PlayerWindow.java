package pushgame.gui.windows;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pushgame.players.PlayerFactory;
import pushgame.util.GameConfig;


@SuppressWarnings("serial")
public class PlayerWindow extends JFrame implements ChangeListener,
		ActionListener
{
	JColorChooser cc1 = new JColorChooser();
	JRadioButton rb1 = new JRadioButton("Gracz pierwszy");
	JRadioButton rb2 = new JRadioButton("Gracz drugi");
	ButtonGroup bg = new ButtonGroup();
	JButton prev1 = new JButton();
	JButton prev2 = new JButton();
	JComboBox<String> cb1 = new JComboBox<String>();
	JComboBox<String> cb2 = new JComboBox<String>();

	JComboBox<Integer> md1 = new JComboBox<Integer>();
	JComboBox<Integer> md2 = new JComboBox<Integer>();

	public PlayerWindow()
	{
		super("Pionek");
		setBounds(100, 100, 620, 380);
		setBackground(Color.GRAY);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setEnabled(true);
		setVisible(true);

		rb1.setBounds(10, 5, 112, 30);
		rb1.setSelected(true);
		rb2.setBounds(325, 5, 90, 30);
		bg.add(rb1);
		bg.add(rb2);
		getContentPane().add(rb1);
		getContentPane().add(rb2);

		cc1.setBounds(0, 40, 620, 400);
		getContentPane().add(cc1);
		getContentPane().setLayout(null);
		rb1.addActionListener(this);
		rb2.addActionListener(this);
		cc1.getSelectionModel().addChangeListener(this);
		cc1.setColor(GameConfig.player1Color);

		prev1.setBounds(123, 10, 30, 20);
		prev1.setVisible(true);
		prev1.setBackground(GameConfig.player1Color);
		prev1.setEnabled(false);
		getContentPane().add(prev1);

		
		PlayerFactory.addAlgos(cb1);
		cb1.setBounds(154, 10, 119, 20);
		cb1.setSelectedItem(PlayerFactory.algoAI1);
		cb1.addActionListener(this);
		getContentPane().add(cb1);
		
		md1.setBounds(272, 10, 35, 20);
		PlayerFactory.loadDepths(1,(String) cb1.getSelectedItem(), md1);
		md1.setVisible(md1.getItemCount()>0);
		md1.addActionListener(this);
		getContentPane().add(md1);

		prev2.setBounds(416, 10, 30, 20);
		prev2.setVisible(true);
		prev2.setBackground(GameConfig.player2Color);
		prev2.setEnabled(false);
		getContentPane().add(prev2);


		PlayerFactory.addAlgos(cb2);
		cb2.setBounds(447, 10, 119, 20);
		cb2.setSelectedItem(PlayerFactory.algoAI2);
		cb2.addActionListener(this);
		getContentPane().add(cb2);
		
		md2.setBounds(567, 10, 35, 20);
		PlayerFactory.loadDepths(2,(String) cb2.getSelectedItem(), md2);
		md2.setVisible(md2.getItemCount()>0);
		md2.addActionListener(this);
		getContentPane().add(md2);

	}

	public void stateChanged(ChangeEvent e)
	{
		if (rb1.isSelected())
		{
			GameConfig.player1Color = cc1.getColor();
		}
		if (rb2.isSelected())
		{
			GameConfig.player2Color = cc1.getColor();
		}

		prev1.setBackground(GameConfig.player1Color);
		prev2.setBackground(GameConfig.player2Color);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (rb1.isSelected())
		{
			cc1.setColor(GameConfig.player1Color);
		}
		if (rb2.isSelected())
		{
			cc1.setColor(GameConfig.player2Color);
		}
		Object src = e.getSource();
		if (src == cb1)
		{
			PlayerFactory.setAlgo(1, cb1);
			PlayerFactory.loadDepths(1,(String) cb1.getSelectedItem(), md1);
			md1.setVisible(md1.getItemCount()>0);
		}
		if (src == cb2)
		{
			PlayerFactory.setAlgo(2, cb2);
			PlayerFactory.loadDepths(2,(String) cb2.getSelectedItem(), md2);
			md2.setVisible(md2.getItemCount()>0);
		}
		if (src == md1)
		{
			PlayerFactory.setDepth(1, md1);
		}
		if (src == md2)
		{
			PlayerFactory.setDepth(2, md2);
		}
	}
}
