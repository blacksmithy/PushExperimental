package pushgame.util;

import java.awt.Color;

public class GameConfig {

	public static Color player1Color = Color.blue;
	public static Color player2Color = Color.red;
	public static int delay=250;
	private static short ai1Depth = 0;
	private static short ai2Depth = 0;
	private static boolean sortEnable = true;
	
	
	private static GameConfig config;
	
	
	private GameConfig() {
		
	}
	
	public static GameConfig getInstance() {
		if (config != null)
			return config;
		config = new GameConfig();
		return config;
	}

	public short getAi1Depth() {
		return ai1Depth;
	}

	public void setAi1Depth(short ai1Depth) {
		GameConfig.ai1Depth = ai1Depth;
	}

	public short getAi2Depth() {
		return ai2Depth;
	}

	public void setAi2Depth(short ai2Depth) {
		GameConfig.ai2Depth = ai2Depth;
	}
	
	public boolean isSortEnabled() {
		return sortEnable;
	}
	
	

}
