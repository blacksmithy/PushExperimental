package pushgame.players;

import javax.swing.JComboBox;

import pushgame.util.GameConfig;


public class PlayerFactory
{
	static final public String HUMAN_PLAYER="Gracz";
	static final public String GREEDY_AI="AI zachłanny";
	static final public String ALPHA_BETA_AI="AI alfa-beta";
	static final public String RANDOM_AI="AI losowy";
	static final public String FS_ALPHA_BETA = "AI alfa-beta FS";
	static final public String FS_ALPHA_BETA_TT = "AI alfa-beta FS TT";
	static final public String BLS_TEST = "Algo BLS";
	static final public String ELF_TEST = "Algo ELF";
	
	
	static public String algoAI1=RANDOM_AI;
	static public Integer depthAI1=4;
	static public String algoAI2=HUMAN_PLAYER;
	static public Integer depthAI2=6;
	
	
	static public void addAlgos(JComboBox<String> cb)
	{
		cb.removeAllItems();
		cb.addItem(HUMAN_PLAYER);
		cb.addItem(RANDOM_AI);
		cb.addItem(GREEDY_AI);
		cb.addItem(ALPHA_BETA_AI);
		cb.addItem(FS_ALPHA_BETA);
		cb.addItem(FS_ALPHA_BETA_TT);
//		cb.addItem(ELF_TEST);
//		cb.addItem(BLS_TEST);
	}
	
	static public void loadDepths(int aiID,String algo,JComboBox<Integer> cb)
	{
		if(algo==ALPHA_BETA_AI || algo == FS_ALPHA_BETA || algo == FS_ALPHA_BETA_TT)
		{
			cb.removeAllItems();
			cb.addItem(1);
			cb.addItem(2);
			cb.addItem(3);
			cb.addItem(4);
			cb.addItem(5);
			cb.addItem(6);
			cb.addItem(7);
			if(aiID==1)
			{
				cb.setSelectedItem(depthAI1);
			}
			else if(aiID==2)
			{
				cb.setSelectedItem(depthAI2);
			}
			
		}
		else
		{
			cb.removeAllItems();
		}
	}
	
	static public void setDepth(int aiID,JComboBox<Integer> cb)
	{
		if(cb.getItemCount()>0)
		{
			if(aiID==1)
			{
				depthAI1=(Integer) cb.getSelectedItem();
			}
			else if(aiID==2)
			{
				depthAI2=(Integer) cb.getSelectedItem();
			}
		}
	}
	
	static public void setAlgo(int aiID,JComboBox<String> cb)
	{
		if(aiID==1)
		{
			algoAI1=(String) cb.getSelectedItem();
		}
		else if(aiID==2)
		{
			algoAI2=(String) cb.getSelectedItem();
		}
	}
	
	static public Player getPlayer(byte id, int delay)
	{
		String ai="";
		Integer md=0;
		GameConfig config = GameConfig.getInstance();
		if(id==1)
		{
			ai=algoAI1;
			md=depthAI1;
			config.setAi1Depth(Short.valueOf(depthAI1.toString()));
		}
		else if(id==2)
		{
			ai=algoAI2;
			md=depthAI2;
			config.setAi2Depth(Short.valueOf(depthAI2.toString()));
		}
		
		Player result=null;
		if(ai==HUMAN_PLAYER)
		{
			result=new HumanPlayer(id,delay);
		}
		else if(ai==RANDOM_AI)
		{
			result=new RandomPlayer(id,delay);
		}
		else if(ai==GREEDY_AI)
		{
			result=new GreedyPlayer(id, delay);
		}
		else if(ai == ALPHA_BETA_AI)
		{
			result=new AlphaBetaPlayer(id, delay);
		}
		else if(ai == FS_ALPHA_BETA)
		{
			result = new FsAlphaBetaPlayer(id, delay);
		}
		else if(ai == FS_ALPHA_BETA_TT)
		{
			result = new TtFsAlphaBetaPlayer(id, delay);
		}
		md++;
		return result;
	}
}
