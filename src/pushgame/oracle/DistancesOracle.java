package pushgame.oracle;

import pushgame.logic.Board;
import pushgame.logic.Movement;

public class DistancesOracle extends Oracle {

	public DistancesOracle() {}

	@Override
	public short getProphecy(Board in, Board out, Movement move) {
		short dist1 = 0;
		short dist2 = 0;
		byte val = 0;
		
		for (int i = 0; i < out.getSize(); ++i) {
			if ((val = out.get((byte) i)) != 0) {
				if (val == player1) {
					dist1 += i / 8;
				}
				else {
					dist2 += 7 - i/8;
				}
			}
		}
		
		return (short) (dist1 - dist2);
	}
	
	

}
