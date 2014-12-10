package call.gamemaker.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ProgressDisplay
{
	private JFrame frame;
	private JProgressBar progress;
	private JLabel task;
	
	private double percent;

	public ProgressDisplay()
	{
		int WIDTH = 500;
		int HEIGHT = 125;

		this.frame = new JFrame("Exporting");
		this.frame.setSize(WIDTH, HEIGHT);
		this.frame.setResizable(false);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.setLayout(null);

		this.progress = new JProgressBar();
		this.progress.setMaximum(100);
		this.progress.setMinimum(0);
		this.progress.setStringPainted(true);
		this.progress.setBounds(10, 10, WIDTH - 25, 50);
		this.frame.add(progress);

		this.task = new JLabel("Task: None");
		this.task.setBounds(10, 50 + 10, WIDTH - 10, 30);
		this.frame.add(task);
		
		setTask("");
	}

	public void setTask(String s)
	{
		this.task.setText("Task: " + s);
	}

	public void setProgress(double i)
	{
		percent = i;
		
		update();
	}
	
	public void updateProgress(double i)
	{
		percent += i;
		
		update();
	}
	
	private void update()
	{
		this.progress.setValue((int) percent);
	}

	public double getProgressExact()
	{
		return percent;
	}
	
	public int getProgress()
	{
		return progress.getValue();
	}

	public void display()
	{
		this.frame.setVisible(true);	
	}

	public void dispose()
	{
		this.frame.dispose();
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
}
