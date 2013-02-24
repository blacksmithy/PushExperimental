package pushgame;

import java.awt.EventQueue;

import pushgame.gui.windows.MainMenu;
import pushgame.util.HashHelper;


public class PushExperimental {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//new GameWindow();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new HashHelper(); // compute zobrist
				//System.out.println("UNIQUE_HASH? " + HashHelper.checkUnique());
				new MainMenu();
			}
		});
	}
}
