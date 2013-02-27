package pushgame.oracle;

import pushgame.logic.Board;
import pushgame.logic.Movement;

public class SymmetricDistancesOracle extends Oracle {

	//private static final short WINNER_BONUS = 100;
	private static final short FORWARD_BONUS = 50;
	
	public SymmetricDistancesOracle() {}

	@Override
	public short getProphecy(Board in, Board out, Movement move, byte player) {
		short dist1 = 0;
		short dist2 = 0;
		byte val = 0;
		
		// 7 | 0 | 1
		// 6 | X | 2
		// 5 | 4 | 3
		
		for (int i = 0; i < 64; ++i) {
			if ((val = out.get((byte) i)) != 0) {
				if (val == player1) {
					dist1 += (i / 8);
				}
				else if (val == player2) {
					dist2 += (7 - i/8);
				}
			}
		}
		
		if (player == player1)
			return (short) (dist1 - dist2);
		else
			return (short) (dist2 - dist1);
	}

}
