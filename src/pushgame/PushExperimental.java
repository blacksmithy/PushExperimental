package pushgame;

import java.awt.EventQueue;

import pushgame.gui.windows.GameWindow;
import pushgame.gui.windows.MainMenu;


public class PushExperimental {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//new GameWindow();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainMenu();
			}
		});
	}
}
