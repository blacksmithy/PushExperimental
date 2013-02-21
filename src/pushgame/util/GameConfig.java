package pushgame.util;

import java.awt.Color;

public class GameConfig {

	public static Color player1Color = Color.blue;
	public static Color player2Color = Color.red;
	private static GameConfig config;
	
	private GameConfig() {
		
	}
	
	public GameConfig getInstance() {
		if (config != null)
			return config;
		config = new GameConfig();
		return config;
	}

}
