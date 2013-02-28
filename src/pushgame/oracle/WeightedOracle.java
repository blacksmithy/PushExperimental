package pushgame.oracle;

import pushgame.logic.Board;
import pushgame.logic.Movement;

public class WeightedOracle extends Oracle {

	private static final short[] FIELD_WEIGHT_PLAYER1 = new short[] {
		0, 0, 38, 60, 76, 88, 110, 111
	};
	private static final short[] FIELD_WEIGHT_PLAYER2 = new short[] {
		111, 110, 88, 76, 60, 38, 0, 0
	};
	
	public WeightedOracle() {}

	@Override
	public short getProphecy(Board in, Board out, Movement move, byte player) {
		short val1 = 0;
		short val2 = 0;
		byte val = 0;

		for (byte i = 0; i < 64; ++i) {
			if ((val = out.get(i)) != 0) {
				if (player == player1 && val == player1) {
					val1 += FIELD_WEIGHT_PLAYER1[i/8];
				}
				else if (player == player2 && val == player2){
					val2 += FIELD_WEIGHT_PLAYER2[i/8];
				}
			}
		}
		
		if (player == player1)
			return val1;
		else
			return val2;
	}

}
