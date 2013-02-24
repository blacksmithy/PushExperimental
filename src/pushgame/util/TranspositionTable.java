package pushgame.util;

import java.util.concurrent.ConcurrentHashMap;

public class TranspositionTable {

	private ConcurrentHashMap<Long, Transposition> map;
	
	public TranspositionTable() {
		map = new ConcurrentHashMap<Long, Transposition>();
	}
	
	public Transposition get(long hash) {		
		return map.get(hash);
	}
	
	public void put(Transposition transposition, long hash) {
		if (map.contains(hash)) {
			map.remove(hash);
		}
		map.put(hash, transposition);
	}
	
	public void remove(long hash) {
		if (map.contains(hash)) {
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
