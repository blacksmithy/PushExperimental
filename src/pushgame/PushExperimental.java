package pushgame;

import java.awt.EventQueue;

import pushgame.gui.windows.MainMenu;
import pushgame.util.HashHelper;


public class PushExperimental {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new HashHelper(); // compute zobrist
				new MainMenu();
			}
		});
	}
}
