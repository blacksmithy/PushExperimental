package pushgame.logic;

import pushgame.util.GlobalSettings;

public class Movement {
	/**
	 * Origin field. Values: <0, 63>.
	 */
	protected byte origin;
	/**
	 * Destination field. Values: <0, 63>.
	 */
	protected byte destination;
	/**
	 * Distance of move. Values: <1, 3>.
	 */
	protected byte distance;
	/**
	 * Angle of move. Values:<br>
	 * 7 | 0 | 1 <br>
	 * 6 | X | 2 <br>
	 * 5 | 4 | 3
	 */
	protected byte angle;
	/**
	 * Number of pawns to push.
	 */
	protected byte chain;
	protected static byte boardSize = GlobalSettings.BOARD_SIZE;
	/**
	 * Square of board's width. Used in movement validation.
	 */
	protected static byte boardSizeSize = GlobalSettings.BOARD_SIZE
			* GlobalSettings.BOARD_SIZE;


	/**
	 * Movement constructor. Computes destination field from given values.
	 * @param from
	 * @param distance
	 * @param angle
	 * @param chainNum
	 */
	public Movement(byte from, byte distance, byte angle, byte chainNum) {
		origin = from;
		this.angle = angle;
		this.chain = chainNum;
		this.distance = distance;
		
		switch (angle) {
		
		case 0:
			destination = (byte) (from - (8 * distance));
			break;
			
		case 1:
			destination = (byte) (from - (8 * distance) + distance);
			break;
			
		case 2:
			destination = (byte) (from + distance);
			break;
			
		case 3:
			destination = (byte) (from + (8 * distance) + distance);
			break;
			
		case 4:
			destination = (byte) (from + (8 * distance));
			break;
			
		case 5:
			destination = (byte) (from + (8 * distance) - distance);
			break;
		
		case 6:
			destination = (byte) (from - distance);
			break;
			
		case 7:
			destination = (byte) (from - (8 * distance) - distance);
			break;
		}
		
		if (origin < 0 || origin > 63 || destination < 0 || destination > 63) {
			System.out.println("ERROR: origin=" + origin + ", dest=" + destination + ", angle=" + angle + ", distance=" + distance + ", chain=" + chain);
		}
	}

	
	/**
	 * Checks if the move is valid (checking all it's parameters).<br>
	 * By default movements <b>are not</b> prevented from being invalid.<br>
	 * <i>Most often there is no need to check it (they are checked in places<br>
	 * where the are creating).</i>
	 * @return True if movement is valid. False if movement is not valid.
	 */
	public boolean isValid() {
		if (origin >= 0 && origin < 64 && destination >= 0 && destination < 64
				&& distance > 0 && distance < 4)
			return true;
		return false;
	}

	
	/**
	 * Returns origin field of movement.
	 * @return Origin field.
	 */
	public byte getOrigin() {
		return origin;
	}


	/**
	 * Returns destination field of movement.
	 * @return Destination field.
	 */
	public byte getDestination() {
		return destination;
	}


	/**
	 * Returns distance of the move.
	 * @return Movement's <1, 3>.
	 */
	public byte getDistance() {
		return distance;
	}


	/**
	 * Returns angle of movement. Values:<br>
	 * 7 | 0 | 1 <br>
	 * 6 | X | 2 <br>
	 * 5 | 4 | 3
	 * @return Movement's angle.
	 */
	public byte getAngle() {
		return angle;
	}


	/**
	 * Returns chain length - number of pawns to be pushed in this move.
	 * @return Chain length.
	 */
	public byte getChain() {
		return chain;
	}


	/**
	 * Returns board size.
	 * @return Board size.
	 */
	public static byte getBoardSize() {
		return boardSize;
	}


	/**
	 * Returns square of board size.
	 * @return Board size ^ 2.
	 */
	public static byte getBoardSizeSize() {
		return boardSizeSize;
	}


	@Override
	public String toString() {
		return "Movement [origin=" + origin + ", destination=" + destination
				+ ", distance=" + distance + ", angle=" + angle + ", chain="
				+ chain + "]";
	}
}
