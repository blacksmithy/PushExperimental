package pushgame.oracle;

import pushgame.logic.Board;
import pushgame.logic.Movement;

public class SymmetricWeightedOracle extends Oracle {

	private static final short[] FIELD_WEIGHT_PLAYER1 = new short[] {
		0, 0, 1, 3, 5, 7, 11, 13
	};
	private static final short[] FIELD_WEIGHT_PLAYER2 = new short[] {
		13, 11, 7, 5, 3, 1, 0, 0
	};
	
	public SymmetricWeightedOracle() {}

	@Override
	public short getProphecy(Board in, Board out, Movement move, byte player) {
		short val1 = 0;
		short val2 = 0;
		byte val = 0;

		for (byte i = 0; i < 64; ++i) {
			if ((val = out.get(i)) != 0) {
				if (val == player1) {
					val1 += FIELD_WEIGHT_PLAYER1[i/8];
				}
				else {
					val2 += FIELD_WEIGHT_PLAYER2[i/8];
				}
			}
		}
		
		if (player == player1)
			return (short) (val1 - val2);
		else
			return (short) (val2 - val1);
	}

}
