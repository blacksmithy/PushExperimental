package pushgame.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import pushgame.util.GlobalSettings;

public class InteractiveBoardPanel extends BoardPanel implements MouseListener
{
	private static final long serialVersionUID = -8916699360484261096L;
	List<Byte> posibleDest=null;
	Byte currField=null;
	FieldListener supervisor=null;

	public InteractiveBoardPanel()
	{
		super();
		addMouseListener(this);
	}
	
	@Override
	protected void drawFields(Graphics2D g)
	{
		super.drawFields(g);
		int fieldSize = GlobalSettings.GUI_BOARD_FIELD_SIZE;
		
		if(currField!=null)
		{
			int row=currField/GlobalSettings.BOARD_SIZE;
			int col=currField%GlobalSettings.BOARD_SIZE;
			g.setColor(Color.gray);
			g.fillRect(fieldSize * col, row * fieldSize, fieldSize, fieldSize);
			g.setColor(Color.black);
			g.drawRect(fieldSize * col, row * fieldSize, fieldSize, fieldSize);
		}
		if(posibleDest!=null)
		{
			int size = GlobalSettings.GUI_BOARD_FIELD_SIZE;
			int checkerSize = GlobalSettings.GUI_BOARD_FIELD_SIZE-6;
			int offset = (size - checkerSize) / 2;
			
			for(int i=0;i<posibleDest.size();i++)
			{	
				int row=posibleDest.get(i)/GlobalSettings.BOARD_SIZE;
				int col=posibleDest.get(i)%GlobalSettings.BOARD_SIZE;
				g.setColor(Color.orange);
				g.fillRect(col * size + offset, row * size + offset, checkerSize, checkerSize);
			}
		}
	}
	
	
	
	public void setFieldListener(FieldListener fl)
	{
		supervisor=fl.getListener();
		System.out.println(supervisor.getClass().getSimpleName());
	}
	public void clearFieldListener()
	{
		supervisor=null;
	}
	
	public void mouseClicked(MouseEvent arg0)
	{
		if(supervisor==null) {return;}
		int fieldSize = GlobalSettings.GUI_BOARD_FIELD_SIZE;
		byte op=0;
		if(arg0.getButton()==MouseEvent.BUTTON1)
		{
			op=FieldListener.SELECT_FIELD;
		}
		else if(arg0.getButton()==MouseEvent.BUTTON3)
		{
			op=FieldListener.EXECUTE_MOV;
		}
		byte row=(byte) (arg0.getY()/fieldSize);
		if((row<0)||(row>=GlobalSettings.BOARD_SIZE)) {return;}
		byte col=(byte) (arg0.getX()/fieldSize);
		if((col<0)||(col>=GlobalSettings.BOARD_SIZE)) {return;}
		
		
		posibleDest=supervisor.fieldClicked(row, col, op);
		if(posibleDest!=null)
		{
			currField=new Byte((byte) ((row*GlobalSettings.BOARD_SIZE)+col));
		}
		else
		{
			currField=null;
		}
		refreshState();
	}


	public void mouseEntered(MouseEvent arg0){}
	public void mouseExited(MouseEvent arg0){}
	public void mousePressed(MouseEvent arg0){}
	public void mouseReleased(MouseEvent arg0){}



}
