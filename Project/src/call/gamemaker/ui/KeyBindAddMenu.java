package call.gamemaker.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import call.gamemaker.KeyBindWrapper;

public class KeyBindAddMenu implements ActionListener
{
	private JFrame frame;
	
	private JTextField name;
	private JSpinner defaultKey;
	
	private JButton add;
	
	private DisplayComponent disp;
	private KeyBindEditMenu menu;
	
	public KeyBindAddMenu(KeyBindEditMenu menu, DisplayComponent dis)
	{
		this.disp = dis;
		this.menu = menu;
		
		int WIDTH = 200;
		int HEIGHT = 150;
		
		this.frame = new JFrame();
		this.frame.setSize(WIDTH, HEIGHT);
		this.frame.setTitle("Add Keybind");
		this.frame.setLocationRelativeTo(null);
		this.frame.setLayout(null);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.frame.setVisible(true);
		
		int x_ = 10;
		int width_ = 75;
		
		int x = 75;
		int width = 100;
		
		int height = 25;
		
		JLabel NAME_ = new JLabel("Name: ");
		NAME_.setBounds(x_, 10, width_, height);
		this.frame.add(NAME_);

		this.name = new JTextField();
		this.name.setBounds(x, 10, width, height);
		this.frame.add(name);

		JLabel KEY_ = new JLabel("Default Key: ");
		KEY_.setBounds(x_, 45, width_, height);
		this.frame.add(KEY_);
		
		
		this.defaultKey = new JSpinner();
		this.defaultKey.setBounds(x, 45, width, height);
		this.frame.add(defaultKey);
		
		
		this.add = new JButton("Add");
		this.add.setBounds(x, 80, width, height);
		this.add.setActionCommand("add");
		this.add.addActionListener(this);
		this.frame.add(add);
		
		this.frame.repaint();
		this.frame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		String com = ae.getActionCommand();
		
		if(com.equals("add"))
		{
			this.disp.addKeyBind(new KeyBindWrapper(name.getText(), Integer.parseInt(defaultKey.getValue().toString())));
			
			this.menu.addValue(name.getText());
			
			this.frame.dispose();
		}
	}
}
