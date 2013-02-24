package pushgame.util;

public class HashHelper {
	
	public static long[][] ZOBRIST_KEYS;
	public static final long RANDOM_LONG = 0xdeadbeef;
	private static long x;

	public HashHelper() {
		ZOBRIST_KEYS = new long[2][64];
		x = RANDOM_LONG;
		for (int i = 0; i < 64; ++i) {
			x ^= (x << 21);
			x ^= (x >>> 35);
			x ^= (x << 4);
			ZOBRIST_KEYS[0][i] = x;
			x ^= (x << 21);
			x ^= (x >>> 35);
			x ^= (x << 4);
			ZOBRIST_KEYS[1][i] = x;
		}
	}
	
	public static boolean checkUnique() {
		long a, b;
		boolean found = false;
		for (int i = 0; i < 64; ++i) {	
			a = ZOBRIST_KEYS[0][i];
			b = ZOBRIST_KEYS[0][i];
			for (int j = i+1; j < 64; ++j) {
				if (ZOBRIST_KEYS[0][j] == a || ZOBRIST_KEYS[1][j] == b) {
					found = true;
					return false;
				}
			}
		}
		if (!found)
			return true;
		return false;		
	}

}
