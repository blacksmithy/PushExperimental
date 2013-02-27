package pushgame.players;

import java.util.List;

import pushgame.gui.boardpanel.FieldListener;
import pushgame.logic.Board;
import pushgame.logic.Movement;

public abstract class Player implements FieldListener {

	protected byte id;
	protected int delay;
	
	protected long statsVisitedNodes;
	protected long statsMovesNum;
	
	public Player(byte id, int delay) {
		this.id = id;
		this.delay = delay;
		
		this.statsVisitedNodes = 0;
		this.statsMovesNum = 0;
	}
	
	public abstract Movement makeMove(Board board);

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public long getStatsVisitedNodes() {
		return statsVisitedNodes;
	}

	public long getStatsMovesNum() {
		return statsMovesNum;
	}

	public List<Byte> fieldClicked(byte row,byte col,byte op) {
		return null;
	}
	public FieldListener getListener() {
		return this;
	}
	public void quit(){}
}
