package pushgame.gui;

import java.util.List;

public interface FieldListener
{
	static final byte SELECT_FIELD=1;
	static final byte EXECUTE_MOV=2;
	public List<Byte> fieldClicked(byte row,byte col,byte op);
	public FieldListener getListener();
}
