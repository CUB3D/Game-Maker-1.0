package call.gamemaker.ui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import call.file.api.CFile;
import call.file.api.FileAPI;
import call.file.layout.Element;
import call.file.layout.Value;
import call.file.writers.BasicWriter;
import call.game.image.AnimatedSprite;
import call.game.image.Animation;
import call.game.image.Image;
import call.game.image.Sprite;
import call.game.utils.AnimationIO;
import call.gamemaker.EntityWrapper;

public class EntityAddMenu implements ActionListener
{
	private JFrame frame;
	private JSpinner X;
	private JSpinner Y;
	private JCheckBox animated;
	private JLabel image_;
	private JTextField image;
	private JButton browse;
	private JTextField name;
	private JTextField tag;
	private JTextField ID;
	private JCheckBox prefab;
	private JButton add;
	private JSpinner health;

	private File imageFile;

	private DisplayComponent component;

	public EntityAddMenu(DisplayComponent component)
	{
		int WIDTH = 310;
		int HEIGHT = 400;

		this.frame = new JFrame();
		this.frame.setSize(WIDTH, HEIGHT);
		this.frame.setTitle("Add Entity");
		this.frame.setLocationRelativeTo(null);
		this.frame.setLayout(null);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		int x_ = 10;
		int x = 75;
		int width_ = 75;
		int width = 100;
		int height = 25;

		JLabel X_ = new JLabel("X: ");
		X_.setBounds(x_, 10, width_, height);
		this.frame.add(X_);

		this.X = new JSpinner();
		this.X.setBounds(x, 10, width, height);
		this.frame.add(X);

		JLabel Y_ = new JLabel("Y: ");
		Y_.setBounds(x_, 45, width_, height);
		this.frame.add(Y_);

		this.Y = new JSpinner();
		this.Y.setBounds(x, 45, width, height);
		this.frame.add(Y);

		JLabel animated_ = new JLabel("Animated: ");
		animated_.setBounds(x_, 80, width_, height);
		this.frame.add(animated_);

		this.animated = new JCheckBox();
		this.animated.setBounds(x, 80, width, height);
		this.animated.setActionCommand("animate");
		this.animated.addActionListener(this);
		this.frame.add(animated);

		image_ = new JLabel("Image: ");
		image_.setBounds(x_, 115, width_, height);
		this.frame.add(image_);

		this.image = new JTextField();
		this.image.setBounds(x, 115, width, height);
		this.frame.add(image);

		this.browse = new JButton("Browse");
		this.browse.setBounds(x + width + 10, 115, width, height);
		this.browse.setActionCommand("browse");
		this.browse.addActionListener(this);
		this.frame.add(browse);

		JLabel name_ = new JLabel("Name: ");
		name_.setBounds(x_, 150, width_, height);
		this.frame.add(name_);

		this.name = new JTextField();
		this.name.setBounds(x, 150, width, height);
		this.frame.add(name);
		
		JLabel tag_ = new JLabel("Tag: ");
		tag_.setBounds(x_, 185, width_, height);
		this.frame.add(tag_);
		
		this.tag = new JTextField();
		this.tag.setBounds(x, 185, width, height);
		this.frame.add(tag);

		JLabel ID_ = new JLabel("ID: ");
		ID_.setBounds(x_, 220, width_, height);
		this.frame.add(ID_);

		this.ID = new JTextField();
		ID.setBounds(x, 220, width, height);
		this.frame.add(ID);

		JLabel prefab_ = new JLabel("Prefab: ");
		prefab_.setBounds(x_, 255, width_, height);
		this.frame.add(prefab_);

		this.prefab = new JCheckBox();
		this.prefab.setBounds(x, 255, width, height);
		this.frame.add(prefab);
		
		JLabel health_ = new JLabel("Health: ");
		health_.setBounds(x_, 290, width_, height);
		this.frame.add(health_);
		
		this.health = new JSpinner();
		this.health.setBounds(x, 290, width, height);
		this.frame.add(health);

		this.add = new JButton("Add");
		this.add.setBounds(x, 325, width, height);
		this.add.setActionCommand("add");
		this.add.addActionListener(this);
		this.frame.add(add);

		this.frame.setVisible(true);

		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent ae) 
	{
		String command = ae.getActionCommand();

		if(command.equals("browse"))
		{
			JFileChooser browse = new JFileChooser(this.component.getWorkspace());

			browse.setMultiSelectionEnabled(false);
			browse.setFileSelectionMode(JFileChooser.FILES_ONLY);

			int i = browse.showOpenDialog(frame);

			if(i == JFileChooser.APPROVE_OPTION)
			{
				imageFile = browse.getSelectedFile();

				image.setText(imageFile.getPath());
			}
		}

		if(command.equals("animate"))
		{
			if(animated.isSelected())
			{
				image_.setText("Animation: ");
				image.setText("");
				imageFile = null;
			}
			else
			{
				image_.setText("Image: ");
				image.setText("");
				imageFile = null;
			}
		}

		if(command.equals("add"))
		{
			int xx = Integer.parseInt(X.getValue().toString());
			int yy = Integer.parseInt(Y.getValue().toString());
			int helt = Integer.parseInt(health.getValue().toString());

			//copy image to sprites dir and update sprite data
			File entitys = new File(component.getWorkspace(), "Entitys");

			File output = new File(entitys, imageFile.getName());

			if(!animated.isSelected())
			{
				BufferedImage img_ = null;

				try
				{
					img_ = ImageIO.read(imageFile);
				}catch(Exception e) {e.printStackTrace();}

				Image img = new Image(img_);

				Sprite s = new Sprite(xx, yy, img);

				try
				{
					output.createNewFile();

					ImageIO.write(img_, "png", output);
				}catch(Exception e) {e.printStackTrace();}

				//update sprite data
				File dataF = new File(entitys, "Data.call");

				CFile cf = new CFile(dataF);

				cf.load();

				Element e = new Element("Entity");

				e.addValue(new Value("X", "" + s.getX()));
				e.addValue(new Value("Y", "" + s.getY()));
				e.addValue(new Value("Image", imageFile.getName()));
				e.addValue(new Value("Name", name.getText()));
				e.addValue(new Value("Prefab", "" + prefab.isSelected()));
				e.addValue(new Value("Animation", "" + animated.isSelected()));
				e.addValue(new Value("ID", ID.getText()));
				e.addValue(new Value("Tag", tag.getText()));
				e.addValue(new Value("Health", "" + helt));

				cf.addElement(e);

				cf.save();

				EntityWrapper sw = new EntityWrapper(s, prefab.isSelected(), imageFile.getName(), name.getText(), tag.getText(), ID.getText(), animated.isSelected(), helt);

				component.addEntity(sw);
			}
			else
			{
				Animation ani = AnimationIO.loadAnimation(imageFile);

				AnimatedSprite s = new AnimatedSprite(xx, yy, ani);

				FileAPI api = new FileAPI(imageFile);

				byte[] b = api.getBytes();

				try
				{
					FileOutputStream stre = new FileOutputStream(output);
					stre.write(b);
					stre.flush();
					stre.close();
				}catch(Exception e) {e.printStackTrace();}

				//update sprite data
				File dataF = new File(entitys, "Data.call");

				boolean f = dataF.exists();

				try
				{
					dataF.createNewFile();
				}catch(Exception e) {e.printStackTrace();}

				CFile cf = new CFile(dataF);

				if(f)
					cf.load();

				Element e = new Element("Entity");

				e.addValue(new Value("X", "" + s.getX()));
				e.addValue(new Value("Y", "" + s.getY()));
				e.addValue(new Value("Image", imageFile.getName()));
				e.addValue(new Value("Animation", "" + animated.isSelected()));
				e.addValue(new Value("Name", name.getText()));
				e.addValue(new Value("Prefab", "" + prefab.isSelected()));
				e.addValue(new Value("ID", "" + ID.getText()));
				e.addValue(new Value("Tag", tag.getText()));
				e.addValue(new Value("Health", "" + helt));

				cf.addElement(e);

				cf.save();

				EntityWrapper sw = new EntityWrapper(s, prefab.isSelected(), imageFile.getName(), name.getText(), tag.getText(), ID.getText(), animated.isSelected(), helt);

				component.addEntity(sw);
			}

			File script = new File(component.getWorkspace(), "Src/code/game/" + name.getText() + ".java");

			try
			{
				script.createNewFile();
			}catch(Exception e) {e.printStackTrace();}

			BasicWriter writer = new BasicWriter(new FileAPI(script));

			writer.writeln("package code.game;");
			writer.writeln("");
			writer.writeln("import call.gamerunner.main.*;");
			writer.writeln("import call.game.entitys.*;");
			writer.writeln("import call.game.image.*;");
			writer.writeln("import call.game.main.*;");
			writer.writeln("import call.game.input.*;");
			writer.writeln("import call.game.input.keyboard.*;");
			writer.writeln("import call.game.input.mouse.*;");
			writer.writeln("");
			writer.writeln("public class " + name.getText());
			writer.writeln("{");
			writer.writeln("");
			writer.writeln("	public BaseEntity entity;");
			writer.writeln("");
			writer.writeln("	public " + name.getText() + "()");
			writer.writeln("	{");
			writer.writeln("	");
			writer.writeln("		EntityWrapper ew = GameHelper.getEntity(\"" + name.getText() + "\");");
			writer.writeln("		entity = ew.getEntity();");
			writer.writeln("	");
			writer.writeln("	}");
			writer.writeln("");
			writer.writeln("	@Define(\"Update\")");
			writer.writeln("	public void update()");
			writer.writeln("	{");
			writer.writeln("		");
			writer.writeln("	}");
			writer.writeln("");
			writer.writeln("}");

			writer.finish();

			try
			{
				Desktop.getDesktop().open(script);
			}catch(IOException e) {e.printStackTrace();}

			frame.dispose();
		}
	}
}