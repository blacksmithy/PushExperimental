package pushgame.oracle;

import pushgame.logic.Board;
import pushgame.logic.Movement;

public class DistancesOracle extends Oracle {

	public DistancesOracle() {}

	@Override
	public short getProphecy(Board in, Board out, Movement move, byte player) {
		short dist1 = 0;
		short dist2 = 0;
		byte val = 0;

		for (int i = 0; i < 64; ++i) {
			if ((val = out.get((byte) i)) != 0) {
				if (player == player1 && val == player1) {
					dist1 += (i / 8);
				}
				else if (player == player2 && val == player2) {
					dist2 += (7 - i/8);
				}
			}
		}
		if (player == player1)
			return dist1;
		else
			return dist2;
	}

}
