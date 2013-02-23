package pushgame.gui.windows;

import java.util.Date;



/**
 * Licznik do odmierzania sumarycznego czasu wykorzystanego przez przez gracza. 
 * @author micmax93
 */
public class Counter extends Thread
{
	int time[] = new int[3];
	long ltime[] = new long[3];
	int turn = 1;
	int count = 0;
	boolean active;
	GameWindow home;

	/**
	 * Zwraca czas dla danego gracza.
	 * @param i
	 * Identyfikator gracza.
	 * @return
	 * Czas w sekundach.
	 */
	public int getTime(int i)
	{
		return time[i];
	}

	/**
	 * Zwraca czas dla danego gracza.
	 * @param i
	 * Identyfikator gracza.
	 * @return
	 * Czas w postaci sformatowanej.
	 */
	public String getTimeString(int i)
	{
		return ((new Integer(time[i] / 60).toString()) + ":" + (new Integer(
				(time[i] / 10) % 6).toString()))
				+ (new Integer(time[i] % 10).toString());
	}
	
	/**
	 * Ustawienie aktualnego gracza.
	 * @param i
	 * Identyfikator gracza.
	 */
	synchronized public void setTurn(int i)
	{
		turn = i;
		commitTime();
		if(turn==1)
		{
			home.getGraphics().drawRect(570, 49, 120, 130);
		}
		else if(turn==2)
		{
			home.getGraphics().drawRect(570, 419, 120, 130);
		}
	}
	
	long last=0,curr=0;
	synchronized void commitTime()
	{
		if(curr==0) {return;} 
		last=curr;
		curr=new Date().getTime();
		ltime[turn] += count*(curr-last);
		time[turn] = (int)(ltime[turn]/1000);
		home.timer[1].setText(getTimeString(1));
		home.timer[3].setText(getTimeString(2));
		if(home.getGraphics()==null) {return;}
	}

	/**
	 * G��wna p�tla w�tku.
	 */
	public void run()
	{
		while (active)
		{
			curr=new Date().getTime();
			try
			{
				sleep(300);
			}
			catch (InterruptedException e)
			{
			}
			commitTime();
		}
	}
	
	/**
	 * Wyzerowanie licznika.
	 */
	public void reset()
	{
		time[0] = 1;
		time[1] = 1;
		time[2] = 1;
	}
	
	/**
	 * Wstrzymanie licznika
	 */
	public void pause()
	{
		count = 0;
	}
	
	/**
	 * Wznowienie licznika
	 */
	public void unpause()
	{
		count = 1;
	}

	/**
	 * Utworzenie licznika dla danego okna gry.
	 * @param root
	 */
	public Counter(GameWindow root)
	{
		super();
		home = root;
		active = true;
		reset();
		count=1;
	}

	/**
	 * Zako�czenie pracy w�tku.
	 */
	synchronized public void finish()
	{
		active = false;
	}
}
