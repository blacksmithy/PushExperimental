package pushgame.util;

public interface AlphaBetaThreadEndEvent {
	public void onThreadEnd(byte threadId, short value);
	public void onThreadEndCountNodes(long nodes);
}
