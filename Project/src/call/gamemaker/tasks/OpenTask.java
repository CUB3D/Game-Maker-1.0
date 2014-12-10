package call.gamemaker.tasks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import call.file.api.CFile;
import call.file.layout.Element;
import call.game.entitys.BasicEntity;
import call.game.image.AnimatedSprite;
import call.game.image.Image;
import call.game.image.Sprite;
import call.game.utils.AnimationIO;
import call.gamemaker.EntityWrapper;
import call.gamemaker.MakerFrame;
import call.gamemaker.SpriteWrapper;
import call.gamemaker.ui.DisplayComponent;

public class OpenTask extends BaseTask
{
	private File dir;

	private MakerFrame frame;

	public OpenTask(DisplayComponent component, MakerFrame frame)
	{
		super(component);
		
		chooseDir();

		this.frame = frame;
	}

	@Override
	public List<Task> getTasks()
	{
		List<Task> tasks = new ArrayList<Task>();

		tasks.add(bt -> setupWorkspace());
		tasks.add(bt -> loadSprites());
		tasks.add(bt -> loadEntitys());
		tasks.add(bt -> finish());

		return tasks;
	}

	public void chooseDir()
	{
		JFileChooser browse = new JFileChooser(new File("."));

		browse.setMultiSelectionEnabled(false);
		browse.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int i = browse.showOpenDialog(display);

		if(i == JFileChooser.APPROVE_OPTION)
			dir = browse.getSelectedFile();
	}

	public void setupWorkspace()
	{
		display.cleanup();

		display.setWorkspace(dir);
	}

	public void loadSprites()
	{
		progress.setTask("Load Sprites");

		File spriteData = new File(dir, "Sprites/Data.call");

		CFile cf = new CFile(spriteData);

		if(cf.getElements().size() > 0)
		{
			double percent = 100 / cf.getElements().size();

			for(Element e : cf.getElements())
			{
				if(e.getName().equals("Sprite"))
				{
					int x = e.getValue("X").getInt(0);
					int y = e.getValue("Y").getInt(0);

					String imageS = e.getValue("Image").getValue();
					String name = e.getValue("Name").getValue();

					boolean prefab = e.getValue("Prefab").getBoolean(false);

					BufferedImage image = null;

					System.out.println(dir.getAbsoluteFile().getName());

					try
					{
						image = ImageIO.read(new File(dir, "Sprites/" + imageS));
					}catch(Exception ee) {ee.printStackTrace();}

					Sprite s = new Sprite(x, y, new Image(image));

					SpriteWrapper sw = new SpriteWrapper(s, prefab, imageS, name);

					display.addSprite(sw);

					progress.updateProgress(percent);
				}
			}
		}
	}

	public void loadEntitys()
	{
		progress.setTask("Load Entitys");

		File entityData = new File(dir, "Entitys/Data.call");


		CFile cf = new CFile(entityData);


		if(cf.getElements().size() > 0)
		{
			double percent = 100 / cf.getElements().size();

			for(Element e : cf.getElements())
			{
				if(e.getName().equals("Entity"))
				{
					int x = e.getValue("X").getInt(0);
					int y = e.getValue("Y").getInt(0);
					int id = e.getValue("ID").getInt(0);

					boolean animation = e.getValue("Animation").getBoolean(false);
					boolean prefab = e.getValue("Prefab").getBoolean(false);

					String imageName = e.getValue("Image").getValue();
					String name = e.getValue("Name").getValue();
					String tag = e.getValue("Tag").getValue();

					File spriteLoc = new File(dir, "Entitys/" + imageName);

					Sprite s = null;

					if(!animation)
					{
						try
						{
							BufferedImage image = ImageIO.read(spriteLoc);

							s = new Sprite(x, y, new Image(image));

						}catch(Exception ee) {ee.printStackTrace();}
					}
					else
						s = new AnimatedSprite(x, y, AnimationIO.loadAnimation(spriteLoc));

					EntityWrapper sw = new EntityWrapper(new BasicEntity(s, id), prefab, imageName, name, tag, id, animation);

					display.addEntity(sw);

					progress.updateProgress(percent);
				}
			}
		}
	}

	public void finish()
	{
		frame.export.setEnabled(true);
		frame.publish.setEnabled(true);

		frame.add.setEnabled(true);

		frame.engine.setEnabled(true);
	}
}
