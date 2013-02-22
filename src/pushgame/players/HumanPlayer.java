package pushgame.players;

import java.util.ArrayList;
import java.util.List;

import pushgame.gui.FieldListener;
import pushgame.logic.Board;
import pushgame.logic.Movement;
import pushgame.util.GlobalSettings;

public class HumanPlayer extends Player {

	FieldObserver obs;

	public HumanPlayer(byte id, int delay) {
		super(id, delay);
		obs=new FieldObserver(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Movement makeMove(Board board) {
		obs.init(board);
		obs.start();
		
		try
		{
			obs.join();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("joined");
		return obs.result;
	}


	public FieldListener getListener() {
		obs=new FieldObserver(id);
		return obs;
	}

}

class FieldObserver extends Thread implements FieldListener
{
	byte id;
	boolean active=true;
	Board currBoard;
	Byte currField=null;
	List <Movement> movs=null;
	List<Byte> dests=null;
	Movement result=null;
	
	public FieldObserver(byte id)
	{
		super();
		currField=null;
		movs=null;
		result=null;
		dests=null;
		this.id=id;
	}

	public void init(Board board)
	{
		currField=null;
		movs=null;
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
			if(currBoard.get(field)==id)
			{
				currField=new Byte(field);
				movs=currBoard.getPossibleMoves(id);
				dests=new ArrayList<Byte>();
				for(int i=0;i<movs.size();i++)
				{
					if(movs.get(i).getOrigin()==currField)
					{
						dests.add(movs.get(i).getDestination());
					}
				}
				return dests;
			}
			else
			{
				currField=null;
				movs=null;
				dests=null;
				return null;
			}
		}
		else if(op==FieldListener.EXECUTE_MOV)
		{
			if(movs!=null)
			{
				for(int i=0;i<movs.size();i++)
				{
					if(movs.get(i).getOrigin()==currField)
					{
						if(movs.get(i).getDestination()==field)
						{
							result=movs.get(i);
							try {return null;}
							finally {active=false;}
						}
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
	
}
