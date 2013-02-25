package pushgame.util;

import java.util.Comparator;

import pushgame.logic.Movement;

public class MovementComparator implements Comparator<Movement> {

	public MovementComparator() {
	}

	@Override
	public int compare(Movement m1, Movement m2) {
		int val1 = m1.getChain() * m1.getDistance();
		int val2 = m2.getChain() * m2.getDistance();
	
		if (val1 > val2)// DESC!!
			return -1;
		else if (val1 == val2)
			return 0;
		return 1;
	}

}
