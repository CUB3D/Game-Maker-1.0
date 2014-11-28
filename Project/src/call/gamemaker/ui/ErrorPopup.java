package call.gamemaker.ui;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ErrorPopup
{
	private JFrame frame;
	private JTextArea label;
	
	public ErrorPopup(String text)
	{
		int WIDTH = 1000;
		int HEIGHT = 250;
		
		this.frame = new JFrame();
		this.frame.setSize(WIDTH, HEIGHT);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.setLayout(null);
		this.frame.setTitle("Error!");
		
		this.label = new JTextArea();
		this.label.setText(text);
		this.label.setBounds(0, 0, WIDTH, HEIGHT);
		this.label.setWrapStyleWord(true);
		this.label.setEditable(false);
		this.frame.add(label);
		
		this.frame.setVisible(true);
	}
	
	public void setText(String s)
	{
		this.label.setText(s);
	}
	
	public String getText()
	{
		return this.label.getText();
	}
			
}
