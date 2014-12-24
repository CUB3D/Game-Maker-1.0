package call.gamemaker.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import call.file.api.CFile;
import call.file.layout.Element;
import call.file.layout.Value;
import call.game.entitys.BasicEntity;
import call.game.image.Sprite;
import call.gamemaker.EntityWrapper;
import call.gamemaker.SpriteWrapper;
import call.gamemaker.ui.DisplayComponent;

public class SaveTask extends BaseTask
{
	public SaveTask(DisplayComponent component)
	{
		super(component, "Saving");
	}
	
	@Override
	public List<Task> getTasks()
	{
		List<Task> tasks = new ArrayList<Task>();
		
		tasks.add(bt -> saveSprites());
		tasks.add(bt -> saveEntitys());
		
		return tasks;
	}
	
	public void saveSprites()
	{
		File sprites = new File(display.getWorkspace(), "Sprites");

		File output = new File(sprites, "Data.call");

		output.delete();

		try
		{
			output.createNewFile();
		}catch(Exception e) {e.printStackTrace();}
		
		

		CFile cf = new CFile(output);

		Element noop = new Element("NOOP");
		noop.addValue(new Value("NOOP", "1"));
		cf.addElement(noop);

		for(SpriteWrapper sw : display.getSprites())
		{
			Sprite s = sw.getSprite();

			Element e = new Element("Sprite");

			e.addValue(new Value("X", "" + s.getX()));
			e.addValue(new Value("Y", "" + s.getY()));
			e.addValue(new Value("Image", sw.getImage()));
			e.addValue(new Value("Name", sw.getName()));
			e.addValue(new Value("Prefab", "" + sw.isPrefab()));

			cf.addElement(e);
		}

		cf.save();
	}
	
	public void saveEntitys()
	{
		File entitys = new File(display.getWorkspace(), "Entitys");

		File output = new File(entitys, "Data.call");

		output.delete();

		try
		{
			output.createNewFile();
		}catch(Exception e) {e.printStackTrace();}

		CFile cf = new CFile(output);

		Element noop = new Element("NOOP");
		noop.addValue(new Value("NOOP", "1"));
		cf.addElement(noop);

		for(EntityWrapper sw : display.getEntitys())
		{
			BasicEntity s = sw.getEntity();

			Element e = new Element("Entity");

			e.addValue(new Value("X", "" + s.getX()));
			e.addValue(new Value("Y", "" + s.getY()));
			e.addValue(new Value("Image", sw.getImage()));
			e.addValue(new Value("Name", sw.getName()));
			e.addValue(new Value("Prefab", "" + sw.isPrefab()));
			e.addValue(new Value("Animation", "" + sw.isAnimated()));
			e.addValue(new Value("ID", sw.getID()));
			e.addValue(new Value("Tag", sw.getTag()));
			e.addValue(new Value("Health", "" + sw.getHealth()));

			cf.addElement(e);
		}

		cf.save();
	}
}
