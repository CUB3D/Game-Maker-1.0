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

	public BaseTask(DisplayComponent component)
	{
		this.display = component;
		
		this.workspace = component.getWorkspace();

		progress = new ProgressDisplay();
	}

	public void excecute()
	{
		progress.display();
		
		BaseTaskRunnable btr = new BaseTaskRunnable(this);
		
		new Thread(btr).start();
	}
	
	public List<Task> getTasks()
	{
		return null;
	}
}
