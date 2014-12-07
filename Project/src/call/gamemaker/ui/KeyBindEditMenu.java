package call.gamemaker.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;

import call.gamemaker.MakerFrame;
import call.utils.arrays.StringArray;

public class KeyBindEditMenu implements ActionListener
{
	private JFrame frame;
	
	private JList<String> list;
	
	private JButton add;
	
	private StringArray array = new StringArray(1);
	
	private DisplayComponent disp;
	
	public KeyBindEditMenu(MakerFrame frame)
	{
		this.disp = frame.testDispaly;
		
		int WIDTH = 250;
		int HEIGHT = 350;
		
		this.frame = new JFrame();
		this.frame.setSize(WIDTH, HEIGHT);
		this.frame.setTitle("Edit Entity");
		this.frame.setLocationRelativeTo(null);
		this.frame.setLayout(null);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.list = new JList<String>();
		this.list.setBounds(10, 10, 100, 150);
		
		this.frame.add(list);
		
		this.frame.setVisible(true);
		
		this.add = new JButton("Add");
		this.add.setBounds(10, 170, 100, 25);
		this.add.setActionCommand("Add");
		this.add.addActionListener(this);
		this.frame.add(add);
		
		
		addValue("ABC");
		addValue("ABC");
		addValue("ABC");
		
	}
	
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		String com = ae.getActionCommand();
		
		if(com.equals("Add"))
		{
			new KeyBindAddMenu(this, disp);
		}
	}
	
	
	public void addValue(String name)
	{
		array.add(name);
		
		String[] s = array.getArray();
		String[] s1 = new String[s.length - 1];
		
		System.arraycopy(s, 0, s1, 0, s.length - 1);
		
		list.setListData(s1);
	}
}
