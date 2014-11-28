package call.gamemaker.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import call.file.api.CFile;
import call.file.layout.Element;
import call.file.layout.Value;
import call.game.image.Image;
import call.game.image.Sprite;
import call.gamemaker.SpriteWrapper;

public class SpriteAddMenu implements ActionListener
{
	private JFrame frame;

	private JTextField path;

	private JSpinner x;
	private JSpinner y;

	private JTextField name;

	private JCheckBox prefab;

	private File file;

	private DisplayComponent component;

	public SpriteAddMenu(DisplayComponent component)
	{
		this.component = component;

		frame = new JFrame("Add sprite");
		frame.setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setMinimumSize(new Dimension(275, 250));
		frame.setResizable(false);

		int lx = 10;
		int x_ = 60;
		int height = 25;

		JLabel X = new JLabel("X: ");
		X.setBounds(lx, 10, 25, height);
		frame.add(X);

		x = new JSpinner();
		x.setBounds(x_, 10, 100, height);
		frame.add(x);

		JLabel Y = new JLabel("Y: ");
		Y.setBounds(lx, 40, 25, height);
		frame.add(Y);

		y = new JSpinner();
		y.setBounds(x_, 40, 100, height);
		frame.add(y);

		JLabel PATH = new JLabel("Image: ");
		PATH.setBounds(lx, 70, 50, height);
		frame.add(PATH);

		path = new JTextField();
		path.setBounds(x_, 70, 100, height);
		frame.add(path);

		JButton browse = new JButton("Browse");
		browse.setBounds(x_ + 105, 70, 85, height);
		browse.setActionCommand("browse");
		browse.addActionListener(this);
		frame.add(browse);

		JLabel NAME = new JLabel("Name: ");
		NAME.setBounds(lx, 100, 100, height);
		frame.add(NAME);

		name = new JTextField();
		name.setBounds(x_, 100, 100, height);
		frame.add(name);

		JLabel PREFAB = new JLabel("Prefab: ");
		PREFAB.setBounds(lx, 130, 100, height);
		frame.add(PREFAB);

		prefab = new JCheckBox();
		prefab.setBounds(x_, 130, 100, height);
		frame.add(prefab);

		// Add a name box + label + update output

		JButton add = new JButton("Add");
		add.setBounds(x_, 160, 100, height);
		add.setActionCommand("add");
		add.addActionListener(this);
		frame.add(add);

		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		String com = ae.getActionCommand();

		if(com.equals("browse"))
		{
			JFileChooser browse = new JFileChooser(new File("."));

			browse.setMultiSelectionEnabled(false);
			browse.setFileSelectionMode(JFileChooser.FILES_ONLY);

			int i = browse.showOpenDialog(frame);

			if(i == JFileChooser.APPROVE_OPTION)
			{
				file = browse.getSelectedFile();

				path.setText(file.getPath());
			}
		}

		if(com.equals("add"))
		{
			BufferedImage img_ = null;

			try
			{
				img_ = ImageIO.read(file);
			}catch(Exception e) {}

			Image img = new Image(img_);


			int xx = Integer.parseInt(x.getValue().toString());
			int yy = Integer.parseInt(y.getValue().toString());

			Sprite s = new Sprite(xx, yy, img);

			//copy image to sprites dir and update sprite data
			File pat = new File(path.getText());
			File sprites = new File(component.getWorkspace(), "Sprites");

			File output = new File(sprites, pat.getName());

			try
			{
				output.createNewFile();

				ImageIO.write(img_, "png", output);
			}catch(Exception e) {}

			//update sprite data
			File dataF = new File(sprites, "Data.call");

			boolean f = dataF.exists();

			try
			{
				dataF.createNewFile();
			}catch(Exception e) {e.printStackTrace();}

			CFile cf = new CFile(dataF);

			if(f)
				cf.getElements();

			Element e = new Element("Sprite");

			e.addValue(new Value("X", "" + s.getX()));
			e.addValue(new Value("Y", "" + s.getY()));
			e.addValue(new Value("Image", file.getName()));
			e.addValue(new Value("Name", name.getText()));
			e.addValue(new Value("Prefab", "" + prefab.isSelected()));

			cf.addElement(e);

			cf.save();

			SpriteWrapper sw = new SpriteWrapper(s, prefab.isSelected(), file.getName(), name.getText());

			component.addSprite(sw);

			frame.dispose();
		}
	}
}
