package pushgame.util;

import pushgame.logic.Movement;

public class Transposition {

	public static final byte VALUE_LOWER = (byte) 0;
	public static final byte VALUE_ACCURATE = (byte) 1;
	public static final byte VALUE_UPPER = (byte) 2;
	
	private short value;
	private byte type;
	private short depth;
	private Movement nextBest;
	
	
	public Transposition(short value, short depth, byte type, Movement best) {
		this.value = value;
		this.depth = depth;
		this.type = type;
		this.nextBest = best;
	}
	
	public Transposition(short value, short depth, short alpha, short beta, Movement best) {
		this(value, depth, VALUE_ACCURATE, best);
		if (value <= alpha)
			type = VALUE_UPPER;
		else if (value >= beta)
			type = VALUE_LOWER;
	}

	public short getValue() {
		return value;
	}

	public void setValue(short value) {
		this.value = value;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public short getDepth() {
		return depth;
	}

	public void setDepth(short depth) {
		this.depth = depth;
	}

	public Movement getNextBest() {
		return nextBest;
	}

	public void setNextBest(Movement nextBest) {
		this.nextBest = nextBest;
	}	
}
