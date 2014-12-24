package call.gamemaker.tasks;

import java.io.File;
import java.util.List;

import call.gamemaker.ui.DisplayComponent;
import call.gamemaker.ui.ProgressDisplay;

public class BaseTask
{
	protected File workspace;

	protected ProgressDisplay progress;
	
	protected DisplayComponent display;

	protected int curTask = 0;
	
	protected BaseTaskRunnable runnable;
	
	protected Thread taskThread;

	public BaseTask(DisplayComponent component, String title)
	{
		this.display = component;
		
		this.workspace = component.getWorkspace();

		this.progress = new ProgressDisplay(title);
		
		runnable = new BaseTaskRunnable(this);
		
		taskThread = new Thread(runnable);
	}

	public void excecute()
	{
		progress.display();
		
		taskThread.start();
	}
	
	public List<Task> getTasks()
	{
		return null;
	}
}
