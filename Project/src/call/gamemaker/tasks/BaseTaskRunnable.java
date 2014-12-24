package call.gamemaker.tasks;

import javax.swing.JOptionPane;

public class BaseTaskRunnable implements Runnable
{
	private BaseTask bt;

	public boolean pause = false;

	public BaseTaskRunnable(BaseTask bt)
	{
		this.bt = bt;
	}

	@Override
	public void run()
	{
		while(true)
		{
			bt.progress.setProgress(0);
			bt.progress.setTask("");

			Task t = bt.getTasks().get(bt.curTask);

			t.excecute(bt);

			bt.curTask++;

			if(bt.curTask >= bt.getTasks().size())
				break;

			bt.progress.setProgress(100);

			try
			{
				Thread.sleep(5);
			}catch(Exception e) {e.printStackTrace();}
		}

		bt.progress.setTask("Done");
		bt.progress.setProgress(100);

		JOptionPane.showMessageDialog(bt.progress.getFrame(), "Done!", "Done", JOptionPane.INFORMATION_MESSAGE);

		bt.progress.dispose();
	}

}
