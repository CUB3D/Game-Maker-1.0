package call.gamemaker.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import call.file.api.CFile;
import call.file.layout.Element;
import call.file.layout.Value;

public class VarAddMenu implements ActionListener
{
	private JFrame frame;
	private JTextField name;
	private JTextField value;
	private JButton add;

	private DisplayComponent display;

	public VarAddMenu(DisplayComponent comp)
	{
		this.frame = new JFrame("Add Variable");
		this.frame.setLayout(null);
		this.frame.setLocationRelativeTo(null);
		this.frame.setMinimumSize(new Dimension(175, 150));
		this.frame.setResizable(false);

		int x_ = 10;
		int x = 60;
		int width_ = 75;
		int width = 100;
		int height = 25;

		JLabel name_ = new JLabel("Name");
		name_.setBounds(x_, 10, width_, height);
		this.frame.add(name_);

		this.name = new JTextField();
		this.name.setBounds(x, 10, width, height);
		this.frame.add(name);

		JLabel value_ = new JLabel("Value");
		value_.setBounds(x_, 45, width_, height);
		this.frame.add(value_);

		this.value = new JTextField();
		this.value.setBounds(x, 45, width, height);
		this.frame.add(value);

		this.add = new JButton("Add");
		this.add.setBounds(x, 80, width, height);
		this.add.setActionCommand("add");
		this.add.addActionListener(this);
		this.frame.add(add);

		this.frame.setVisible(true);

		this.display = comp;
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		String com = ae.getActionCommand();

		if(com.equals("add"))
		{
			File data = new File(display.getWorkspace(), "Data/Vars.call");

			CFile cf = new CFile(data);
			
			cf.load();

			Element e = new Element("Var");
			
			e.addValue(new Value(name.getText(), value.getText()));
			
			cf.addElement(e);
			
			cf.save();
			
			this.frame.dispose();
		}
	}
}
