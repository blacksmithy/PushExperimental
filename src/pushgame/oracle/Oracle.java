package pushgame.oracle;

import pushgame.logic.Board;
import pushgame.logic.Movement;
import pushgame.util.GlobalSettings;

public abstract class Oracle {
	/**
	 * Player1's id.
	 */
	protected static byte player1 = GlobalSettings.PLAYER_1_ID;
	/**
	 * Player2's id.
	 */
	protected static byte player2 = GlobalSettings.PLAYER_2_ID;
	
	/**
	 * Default constructor.
	 */
	public Oracle() {}
	
	/* ********************************************************************* */
	public abstract short getProphecy(Board in, Board out, Movement move);
	/* ********************************************************************* */
	
	/**
	 * Returns player1 id.
	 * @return PLayer1 id.
	 */
	public static byte getPlayer1Id() {
		return player1;
	}

	/**
	 * Returns player2 id.
	 * @return PLayer2 id.
	 */
	public static byte getPlayer2Id() {
		return player2;
	}
}
