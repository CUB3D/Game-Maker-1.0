package call.gamemaker.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TextTable extends JTable
{
	private static final long serialVersionUID = 8583522549552509011L;

	public List<String> data = new ArrayList<String>();

	public void addString(String s)
	{	
		data.add(s);
		reset();
	}

	private void reset()
	{
		setModel(new DefaultTableModel(data.size(), 1));

		for(int i = 0; i < data.size(); i++)
		{
			setValueAt(data.get(i), i, 0);
		}
		
		setVisible(true);
	}
}
