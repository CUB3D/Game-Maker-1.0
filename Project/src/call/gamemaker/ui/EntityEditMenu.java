package call.gamemaker.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import call.file.api.FileAPI;
import call.game.image.AnimatedSprite;
import call.game.image.Image;
import call.game.image.Sprite;
import call.game.utils.AnimationIO;
import call.gamemaker.EntityWrapper;

public class EntityEditMenu implements ActionListener
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
	private JCheckBox prefab;
	private JButton add;

	private File imageFile;

	private DisplayComponent component;

	private EntityWrapper entity;

	public EntityEditMenu(DisplayComponent component, EntityWrapper ew)
	{
		this.entity = ew;

		int WIDTH = 310;
		int HEIGHT = 305;

		this.frame = new JFrame();
		this.frame.setSize(WIDTH, HEIGHT);
		this.frame.setTitle("Edit Entity");
		this.frame.setLocationRelativeTo(null);
		this.frame.setLayout(null);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		int x_ = 10;
		int x = 80;
		int width_ = x;
		int width = 100;
		int height = 25;

		JLabel X_ = new JLabel("X: ");
		X_.setBounds(x_, 10, width_, height);
		this.frame.add(X_);

		this.X = new JSpinner();
		this.X.setBounds(x, 10, width, height);
		this.X.setValue(entity.getEntity().getX());
		this.frame.add(X);

		JLabel Y_ = new JLabel("Y: ");
		Y_.setBounds(x_, 45, width_, height);
		this.frame.add(Y_);

		this.Y = new JSpinner();
		this.Y.setBounds(x, 45, width, height);
		this.Y.setValue(entity.getEntity().getY());
		this.frame.add(Y);

		JLabel animated_ = new JLabel("Animated: ");
		animated_.setBounds(x_, 80, width_, height);
		this.frame.add(animated_);

		this.animated = new JCheckBox();
		this.animated.setBounds(x, 80, width, height);
		this.animated.setActionCommand("animate");
		this.animated.addActionListener(this);
		this.animated.setSelected(entity.isAnimated());
		this.frame.add(animated);

		image_ = new JLabel("Image: ");
		image_.setBounds(x_, 115, width_, height);
		this.frame.add(image_);

		this.image = new JTextField();
		this.image.setBounds(x, 115, width, height);
		this.image.setText(entity.getImage());
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
		this.name.setText(entity.getName());
		this.frame.add(name);
		
		JLabel tag_ = new JLabel("Tag: ");
		tag_.setBounds(x_, 185, width_, height);
		this.frame.add(tag_);
		
		this.tag = new JTextField();
		this.tag.setBounds(x, 185, width, height);
		this.tag.setText(entity.getTag());
		this.frame.add(tag);

		JLabel prefab_ = new JLabel("Prefab: ");
		prefab_.setBounds(x_, 220, width_, height);
		this.frame.add(prefab_);

		this.prefab = new JCheckBox();
		this.prefab.setBounds(x, 220, width, height);
		this.prefab.setSelected(entity.isPrefab());
		this.frame.add(prefab);

		this.add = new JButton("Save");
		this.add.setBounds(x, 255, width, height);
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
			Sprite s = entity.getEntity().getSprite();
			s.setX(Integer.parseInt(X.getValue().toString()));
			s.setY(Integer.parseInt(Y.getValue().toString()));
			
			entity.setAnimated(animated.isSelected());
			
			entity.setName(name.getText());
			
			entity.setTag(tag.getText());
			
			entity.setPrefab(prefab.isSelected());
			
			if(!image.getText().equals(entity.getImage()))
			{
				File oldImg = new File(component.getWorkspace(), "Entitys/" + entity.getImage());
				
				if(!oldImg.delete())
				{
					System.out.println("Couldent delete file: deleting on exit");
					oldImg.deleteOnExit();
				}
				
				File output = new File(component.getWorkspace(), "Entitys/" + imageFile.getName());
				
				if(!animated.isSelected())
				{	
					BufferedImage img_ = null;

					try
					{
						img_ = ImageIO.read(imageFile);
					}catch(Exception e) {e.printStackTrace();}

					Sprite ss = new Sprite(s.getX(), s.getY(), new Image(img_));
					entity.setEntitySprite(ss);
					
					try
					{
						output.createNewFile();

						ImageIO.write(img_, "png", output);
					}catch(Exception e) {e.printStackTrace();}
				}
				else
				{
					AnimatedSprite ss = new AnimatedSprite(s.getX(), s.getY(), AnimationIO.loadAnimation(imageFile));
					entity.setEntitySprite(ss);
					
					FileAPI api = new FileAPI(imageFile);

					byte[] b = api.getBytes();

					try
					{
						FileOutputStream stre = new FileOutputStream(output);
						stre.write(b);
						stre.flush();
						stre.close();
					}catch(Exception e) {e.printStackTrace();}
				}
				
				entity.setImage(imageFile.getName());
			}
			
			frame.dispose();
		}
	}
}