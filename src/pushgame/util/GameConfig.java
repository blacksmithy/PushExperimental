package pushgame.util;

import java.awt.Color;

import pushgame.players.PlayerFactory;

public class GameConfig {

	public static Color player1Color = Color.blue;
	public static Color player2Color = Color.red;
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
		return PlayerFactory.depthAI1.shortValue();
	}

	public void setAi1Depth(short ai1Depth) {
		PlayerFactory.depthAI1 = (int) ai1Depth;
	}

	public short getAi2Depth() {
		return PlayerFactory.depthAI2.shortValue();
	}

	public void setAi2Depth(short ai2Depth) {
		PlayerFactory.depthAI2 = (int) ai2Depth;
	}
	
	public boolean isSortEnabled() {
		return sortEnable;
	}
	
	

}
