package call.gamemaker.tasks;

public class BaseTaskRunnable implements Runnable
{
	private BaseTask bt;
	
	public BaseTaskRunnable(BaseTask bt)
	{
		this.bt = bt;
	}

	@Override
	public void run()
	{
		while(true)
		{
			Task t = bt.getTasks().get(bt.curTask);
			
			t.excecute(bt);

			bt.curTask++;

			if(bt.curTask >= bt.getTasks().size())
				break;
			
			try
			{
				Thread.sleep(5);
			}catch(Exception e) {e.printStackTrace();}
		}
	}

}
