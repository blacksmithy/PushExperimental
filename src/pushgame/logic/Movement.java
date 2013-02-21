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
	 * Number of checkers to push.
	 */
	protected byte chain;
	protected static byte boardSize = GlobalSettings.BOARD_SIZE;
	/**
	 * Square of board's width. Used in movement validation.
	 */
	protected static byte boardSizeSize = GlobalSettings.BOARD_SIZE
			* GlobalSettings.BOARD_SIZE;


//	public Movement(byte from, byte to, byte angle, byte chainNum) {
//		origin = from;
//		destination = to;
//		this.angle = angle;
//		chain = chainNum;
//		distance = 1;
//	}

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

	
	
	public byte getOrigin() {
		return origin;
	}



	public byte getDestination() {
		return destination;
	}



	public byte getDistance() {
		return distance;
	}



	public byte getAngle() {
		return angle;
	}



	public byte getChain() {
		return chain;
	}



	public static byte getBoardSize() {
		return boardSize;
	}



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
