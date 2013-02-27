package pushgame.util;

import java.util.HashMap;
//import java.util.HashSet;

public class TranspositionTable {

	private HashMap<Long, Transposition> map;
	//private HashSet<Long> purgatory;
	//private int limit = 5000;
	
	public TranspositionTable() {
		map = new HashMap<Long, Transposition>(1000000);
		//purgatory = new HashSet<Long>();
	}
	
	public Transposition get(long hash) {		
		return map.get(hash);
	}
	
	public void put(Transposition transposition, long hash) {
			if (map.containsKey(hash)) {
				map.remove(hash);
			}
			map.put(hash, transposition);

	}
	
//	public void put(Transposition transposition, long hash) {
//		if (purgatory.size() > limit) {
////			System.out.println("CLEAR!");
//			purgatory.clear();
//		}
//
//		if (purgatory.contains(hash)) { // adding new value to map
//			purgatory.remove(hash);
//			map.put(hash, transposition);
//		}
//		else {
//			if (map.containsKey(hash)) {
//				map.remove(hash);
//				map.put(hash, transposition);
//			}
//			else {
//				purgatory.add(hash);
//			}
//			
//		}
//	}
	
	public void remove(long hash) {
		if (map.containsKey(hash)) {
			map.remove(hash);
		}
	}
	
	public void clear() {
		map.clear();
	}
	
	public int getSize() {
		return map.size();
	}

}
