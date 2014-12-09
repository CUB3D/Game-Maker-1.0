package call.gamemaker.tasks;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import call.file.api.FileAPI;
import call.game.image.Animation;
import call.game.utils.AnimationIO;
import call.gamemaker.ui.DisplayComponent;
import call.gamemaker.ui.ErrorPopup;
import call.utils.CommandLineUtils;

public class ExportTask extends BaseTask
{
	private ZipOutputStream output;

	public ExportTask(File output, DisplayComponent display)
	{
		super(display);

		try
		{
			this.output = new ZipOutputStream(new FileOutputStream(output));
		}catch(Exception e) {e.printStackTrace();}
	}

	@Override
	public List<Task> getTasks()
	{
		List<Task> tasks = new ArrayList<Task>();

		tasks.add(bt -> addSprites());
		tasks.add(bt -> addSpriteData());
		tasks.add(bt -> clean());
		tasks.add(bt -> compileScripts());
		tasks.add(bt -> addScripts());
		tasks.add(bt -> addEntitys());
		tasks.add(bt -> addEntityData());
		tasks.add(bt -> addVars());
		tasks.add(bt -> done());


		return tasks;
	}
	
	public void done()
	{
		progress.setTask("Done");
		progress.updateProgress(100);

		JOptionPane.showMessageDialog(progress.getFrame(), "Done!", "Done", JOptionPane.INFORMATION_MESSAGE);

		progress.dispose();

		try
		{
			output.flush();
			output.close();
		}catch(Exception e) {e.printStackTrace();}
	}

	public void addSprites()
	{
		progress.setTask("Adding sprites");
		progress.updateProgress(0);

		putEntry("Sprites/");	

		File sprites = new File(workspace, "Sprites");

		int precentPerFile = 100 / sprites.listFiles().length;

		for(File ff : sprites.listFiles())
		{
			if(ff.getName().endsWith("png"))
			{
				putEntry("Sprites/" + ff.getName());

				ByteArrayOutputStream out = new ByteArrayOutputStream();

				try
				{
					BufferedImage img = ImageIO.read(ff);
					ImageIO.write(img, "PNG", out);
				}catch(Exception e) {e.printStackTrace();}

				writeBytes(out);

				progress.updateProgress(progress.getProgress() + precentPerFile);

			}
		}
	}

	public void addSpriteData()
	{
		progress.setTask("Adding sprite data");
		progress.updateProgress(0);

		File fil = new File(workspace, "Sprites/Data.call");

		FileAPI api = new FileAPI(fil);

		byte[] bytes = api.getBytes();

		System.out.println("Data.call: Bytes read: " + bytes.length + ", bytes on disk: " + fil.length());

		putEntry("Sprites/Data.call");

		writeBytes(bytes);

		progress.updateProgress(100);
	}



	public void compileScripts()
	{
		progress.setTask("Compiling scripts");
		progress.updateProgress(0);

		File scripts = new File(workspace, "Src/code/game");

		if(scripts.listFiles().length == 0)
		{
			progress.updateProgress(100);
			return;
		}

		File bin = new File(workspace, "bin");

		try
		{
			bin.mkdir();
		}catch(Exception e) {e.printStackTrace();}

		String name = workspace.getAbsolutePath() + "/";

		int percentPerFile = 100 / scripts.listFiles().length;

		for(File ff : scripts.listFiles())
		{

			String cmdbit = "javac.exe ";

			String outputbit = "-d \"" + name + "bin\" ";

			String sourcepathbit = "-sourcepath \"" + name + "Src\" ";

			String cpbit = "-cp \"" + name + "Src;libs/GameMaker.jar;libs/Unknown.jar\" ";

			String filepathbit = "\"" + name + "Src/code/game/" + ff.getName() + "\"";

			String command = cmdbit + outputbit + sourcepathbit + cpbit + filepathbit;

			Process p = CommandLineUtils.exec(command);



			progress.updateProgress(progress.getProgress() + percentPerFile);


			InputStream error = p.getErrorStream();

			try
			{
				ErrorPopup popup = null;

				while(true)
				{
					byte b = (byte) error.read();

					if(b == -1) break;

					if(popup == null)
						popup = new ErrorPopup("");

					popup.setText(popup.getText() + (char) b);
				}
			}catch(Exception e) {e.printStackTrace();}
		}
	}

	public void addScripts()
	{
		progress.setTask("Adding Scripts");
		progress.updateProgress(0);

		putEntry("code/");
		putEntry("code/game/");

		File classes = new File(workspace, "bin/code/game");

		if(classes.exists() && classes.listFiles().length > 0)
		{
			System.out.println("Classes Exist: adding to zip");

			int percentPerFile = 100 / classes.listFiles().length;

			for(File claz : classes.listFiles())
			{
				FileAPI api = new FileAPI(claz);

				byte[] bytes = api.getBytes();

				System.out.println("Class: " + claz.getName() + ", Bytes read: " + bytes.length + ", Bytes on disk: " + claz.length() + " has been added.");

				putEntry("code/game/" + claz.getName());
				writeBytes(bytes);

				progress.updateProgress(progress.getProgress() + percentPerFile);

			}
		}
		else
		{
			System.out.println("No Classes exist if scripts have been added check to make surce javac is on your path and you're on windows or linux");
			progress.updateProgress(100);
		}
	}

	public void addEntitys()
	{
		progress.setTask("Adding entitys");
		progress.updateProgress(0);

		putEntry("Entitys/");	

		File sprites = new File(workspace, "Entitys");

		int precentPerFile = 100 / sprites.listFiles().length;

		for(File ff : sprites.listFiles())
		{
			if(ff.getName().endsWith("png")) // add png image
			{
				putEntry("Entitys/" + ff.getName());

				ByteArrayOutputStream out = new ByteArrayOutputStream();

				try
				{
					BufferedImage img = ImageIO.read(ff);
					ImageIO.write(img, "PNG", out);
				}catch(Exception e) {e.printStackTrace();}

				writeBytes(out);

				progress.updateProgress(progress.getProgress() + precentPerFile);

			}

			if(ff.getName().endsWith("zip")) // add zipped animation
			{
				putEntry("Entitys/" + ff.getName());

				ByteArrayOutputStream out = new ByteArrayOutputStream();

				try
				{
					Animation ani = AnimationIO.loadAnimation(ff);
					AnimationIO.writeAnimation(ani, out);
				}catch(Exception e) {e.printStackTrace();}

				writeBytes(out);

				progress.updateProgress(progress.getProgress() + precentPerFile);

			}
		}
	}

	public void addEntityData()
	{
		progress.setTask("Adding entity data");
		progress.updateProgress(0);

		File fil = new File(workspace, "Entitys/Data.call");

		FileAPI api = new FileAPI(fil);

		byte[] bytes = api.getBytes();

		System.out.println("Data.call: Bytes read: " + bytes.length + ", bytes on disk: " + fil.length());

		putEntry("Entitys/Data.call");

		writeBytes(bytes);

		progress.updateProgress(100);
	}

	public void addVars()
	{
		progress.setTask("Adding Variables");
		progress.updateProgress(0);

		File vars = new File(workspace, "Data/Vars.call");

		FileAPI api = new FileAPI(vars);

		byte[] bytes = api.getBytes();

		System.out.println("Data.call: Bytes read: " + bytes.length + ", bytes on disk: " + vars.length());

		putEntry("Data/Vars.call");
		writeBytes(bytes);

		progress.updateProgress(100);
	}

	public void clean()
	{
		progress.setTask("Cleaning bin");
		progress.updateProgress(0);

		File f = new File(workspace, "bin/code/game");

		if(f.exists() && f.listFiles().length > 0)
		{
			int percentPerFile = 100 / f.listFiles().length;

			for(File ff : f.listFiles())
			{
				ff.delete();
				progress.updateProgress(progress.getProgress() + percentPerFile);
			}
		}

		progress.updateProgress(100);
	}


	public void putEntry(String path)
	{
		try
		{
			output.putNextEntry(new ZipEntry(path));
		}catch(Exception e) {e.printStackTrace();}
	}

	public void writeBytes(ByteArrayOutputStream baos)
	{
		writeBytes(baos.toByteArray());
	}

	public void writeBytes(byte[] bytes)
	{
		try
		{
			output.write(bytes);
		}catch(Exception e) {e.printStackTrace();}
	}
}
