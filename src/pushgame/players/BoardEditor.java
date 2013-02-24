package pushgame.players;

import java.util.ArrayList;
import java.util.List;

import pushgame.gui.boardpanel.FieldListener;
import pushgame.logic.Board;
import pushgame.logic.Movement;
import pushgame.util.GlobalSettings;

public class BoardEditor extends Player {

	EditObserver obs;

	public BoardEditor() {
		super((byte) 1, 0);
	}


	public FieldListener getListener() {
		obs=new EditObserver();
		return obs;
	}

	@Override
	public Movement makeMove(Board board) {
		obs.init(board);
		obs.start();
		
		try {obs.join();}
		catch (InterruptedException e)
		{e.printStackTrace();}

		return null;
	}

}

class EditObserver extends Thread implements FieldListener
{
	boolean active=true;
	Board currBoard;
	Byte currField=null;
	List<Byte> dests=null;
	Movement result=null;
	
	public EditObserver()
	{
		super();
		currField=null;
		result=null;
		dests=null;
	}

	public void init(Board board)
	{
		currField=null;
		result=null;
		dests=null;
		currBoard=board;
	}

	@Override
	public List<Byte> fieldClicked(byte row, byte col, byte op)
	{
		if(currBoard==null) {return null;}
		byte field=(byte) ((row*GlobalSettings.BOARD_SIZE)+col);
		
		if(op==FieldListener.SELECT_FIELD)
		{
			if(currBoard.get(field)!=0)
			{
				currField=new Byte(field);
				dests=new ArrayList<Byte>();
				for(byte i=0;i<64;i++)
				{
					if(currBoard.get(i)==0)
					{
						dests.add(i);
					}
				}
				return dests;
			}
			else
			{
				currField=null;
				dests=null;
				return null;
			}
		}
		else if(op==FieldListener.EXECUTE_MOV)
		{
			if(currField!=null)
			{
				if(currBoard.get(currField)!=0)
				{
					if(currBoard.get(field)==0)
					{
						currBoard.set(field, currBoard.get(currField));
						currBoard.set(currField, (byte) 0);
						
						currBoard.updateHashAfterMove(currField, field, currBoard.get(field));
						
						currField=null;
						dests=null;
						return null;
					}
				}
			}
		}
		return null;
	}

	@Override
	public FieldListener getListener()
	{
		return this;
	}
	
	public void run()
	{
		while(active)
		{
			try {sleep(10);}
			catch (InterruptedException e)
			{e.printStackTrace();}
		}
	}

	@Override
	public void quit()
	{
		active=false;
	}
	
}
