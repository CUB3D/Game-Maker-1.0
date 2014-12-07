package call.gamemaker;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import call.file.api.CFile;
import call.file.api.FileAPI;
import call.file.layout.Element;
import call.file.layout.Value;
import call.file.writers.BasicWriter;
import call.game.entitys.BasicEntity;
import call.game.image.AnimatedSprite;
import call.game.image.Animation;
import call.game.image.Image;
import call.game.image.Sprite;
import call.game.main.Unknown;
import call.game.utils.AnimationIO;
import call.gamemaker.ui.DisplayComponent;
import call.gamemaker.ui.EntityAddMenu;
import call.gamemaker.ui.KeyBindEditMenu;
import call.gamemaker.ui.SpriteAddMenu;
import call.gamemaker.ui.TextTable;
import call.gamemaker.ui.VarAddMenu;

public class MakerFrame implements ActionListener
{
	public JFrame frame;
	public DisplayComponent testDispaly;

	private JMenu file;

	private JMenuItem ne;
	private JMenuItem open;
	private JMenuItem save;
	private JMenuItem export;
	private JMenuItem publish;
	private JMenuItem exit;

	private JMenu add;

	private JMenu view;
	
	private JMenu engine;

	private JCheckBoxMenuItem prefabs;
	private JCheckBoxMenuItem animation;
	private JCheckBoxMenuItem wireframe;

	public TextTable table;
	
	public MakerFrame()
	{
		try
		{
			String lf = UIManager.getSystemLookAndFeelClassName();

			LookAndFeel LF = (LookAndFeel) Class.forName(lf).newInstance();

			UIManager.setLookAndFeel(LF);
		}catch(Exception e) {}

		frame = new JFrame();
		frame.setLayout(null);
		frame.setLocation(0, 0);
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		d.height -= 40;
		
		Unknown.frame_size = Unknown.default_frame_size = d;
		frame.setMinimumSize(Unknown.frame_size);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addMouseListener(Unknown.getMouse());
		frame.addMouseMotionListener(Unknown.getMouse());

		Unknown.getMouse().registerMouseListener(new SpriteControlListener(this));
		Unknown.getMouse().registerMouseListener(new EntityControlListener(this));

		JMenuBar bar = new JMenuBar();
		frame.setJMenuBar(bar);

		file = new JMenu("File");
		bar.add(file);

		ne = new JMenuItem("New");
		ne.setActionCommand("new");
		ne.addActionListener(this);
		file.add(ne);

		open = new JMenuItem("Open");
		open.setActionCommand("open");
		open.addActionListener(this);
		file.add(open);

		save = new JMenuItem("Save");
		save.setActionCommand("save");
		save.addActionListener(this);
		file.add(save);

		file.addSeparator();

		export = new JMenuItem("Export");
		export.setActionCommand("export");
		export.addActionListener(this);
		export.setEnabled(false);
		file.add(export);

		publish = new JMenuItem("Publish");
		publish.setActionCommand("publish");
		publish.addActionListener(this);
		publish.setEnabled(false);
		file.add(publish);

		file.addSeparator();

		exit = new JMenuItem("Quit");
		exit.setActionCommand("exit");
		exit.addActionListener(this);
		file.add(exit);


		add = new JMenu("Add");
		add.setEnabled(false);
		bar.add(add);

		JMenuItem sprite = new JMenuItem("Sprite");
		sprite.setActionCommand("addSprite");
		sprite.addActionListener(this);
		add.add(sprite);

		JMenuItem code = new JMenuItem("Script");
		code.setActionCommand("addCode");
		code.addActionListener(this);
		add.add(code);

		JMenuItem entity = new JMenuItem("Entity");
		entity.setActionCommand("addEntity");
		entity.addActionListener(this);
		add.add(entity);

		JMenuItem varible = new JMenuItem("Global var");
		varible.setActionCommand("addVar");
		varible.addActionListener(this);
		add.add(varible);

		
		engine = new JMenu("Engine");
		engine.setEnabled(false);
		bar.add(engine);
		
		JMenuItem editKey = new JMenuItem("Edit KeyBinds");
		editKey.setActionCommand("EditKey");
		editKey.addActionListener(this);
		engine.add(editKey);
		
		
		view = new JMenu("View");
		bar.add(view);

		prefabs = new JCheckBoxMenuItem("Prefabs");
		prefabs.setSelected(true);
		prefabs.addActionListener(this);
		view.add(prefabs);

		animation = new JCheckBoxMenuItem("Animations");
		animation.setSelected(true);
		animation.addActionListener(this);
		view.add(animation);

		wireframe = new JCheckBoxMenuItem("Wire frame");
		wireframe.setSelected(true);
		wireframe.addActionListener(this);
		view.add(wireframe);

		frame.setJMenuBar(bar);

		testDispaly = new DisplayComponent(this);

		testDispaly.setBounds(0, 0, 800, frame.getHeight());
		
		table = new TextTable();
		
		table.setBounds(800, 0, frame.getWidth() - 800, frame.getHeight());
		
		frame.add(table);
		

		frame.add(testDispaly);

		frame.setVisible(true);
		
		while(true)
			frame.repaint();
	}

	public static void main(String[] args)
	{
		new MakerFrame();
	}

	@Override
	public void actionPerformed(ActionEvent av)
	{
		String com = av.getActionCommand();

		if(com.equals("export"))
		{
			JFileChooser browse = new JFileChooser(testDispaly.getWorkspace().getParentFile());

			browse.setMultiSelectionEnabled(false);
			browse.setFileSelectionMode(JFileChooser.FILES_ONLY);

			int i = browse.showSaveDialog(frame);

			if(i == JFileChooser.APPROVE_OPTION)
			{
				Exporter export = new Exporter(browse.getSelectedFile(), testDispaly);
				export.export();
			}
		}

		if(com.equals("save"))
		{
			File sprites = new File(testDispaly.getWorkspace(), "Sprites");

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

			cf.load();

			for(SpriteWrapper sw : testDispaly.getSprites())
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
			
			
			
			
			File entitys = new File(testDispaly.getWorkspace(), "Entitys");

			output = new File(entitys, "Data.call");

			output.delete();

			try
			{
				output.createNewFile();
			}catch(Exception e) {e.printStackTrace();}

			cf = new CFile(output);

			cf.addElement(noop);

			cf.load();

			for(EntityWrapper sw : testDispaly.getEntitys())
			{
				BasicEntity s = sw.getEntity();

				Element e = new Element("Entity");

				e.addValue(new Value("X", "" + s.getX()));
				e.addValue(new Value("Y", "" + s.getY()));
				e.addValue(new Value("Image", sw.getImage()));
				e.addValue(new Value("Name", sw.getName()));
				e.addValue(new Value("Prefab", "" + sw.isPrefab()));
				e.addValue(new Value("Animation", "" + sw.isAnimated()));
				e.addValue(new Value("ID", "" + sw.getID()));
				e.addValue(new Value("Tag", sw.getTag()));

				cf.addElement(e);
			}

			cf.save();
		}

		if(com.equals("publish"))
		{
			JFileChooser browse = new JFileChooser(new File("."));

			browse.setMultiSelectionEnabled(false);
			browse.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int i = browse.showSaveDialog(frame);

			File out = browse.getSelectedFile();

			if(i == JFileChooser.APPROVE_OPTION)
			{
				File exec = new File("libs/GameMaker.jar");

				FileAPI exec_ = new FileAPI(exec);

				byte[] bytes = exec_.getBytes();

				File execOut = new File(out, "GameRunner.jar");

				try
				{
					execOut.createNewFile();

					FileOutputStream execfos = new FileOutputStream(execOut);
					execfos.write(bytes);
					execfos.flush();
					execfos.close();
				}catch(Exception e) {e.printStackTrace();}

				File info = new File(out, "GameInfo.call");

				try
				{
					info.createNewFile();
				}catch(Exception e) {e.printStackTrace();}

				CFile cf = new CFile(info);

				Element gameinfo = new Element("GameInfo");

				gameinfo.addValue(new Value("TickSpeed", "120"));

				cf.addElement(gameinfo);

				Element window = new Element("Window");

				window.addValue(new Value("Width", "512"));
				window.addValue(new Value("Height", "512"));
				window.addValue(new Value("Title", "TEST"));
				window.addValue(new Value("Resizable", "false"));

				cf.addElement(window);

				cf.save();

				File game = new File(out, "test.game");

				try
				{
					game.createNewFile();
				}catch(Exception e) {e.printStackTrace();}

				Exporter exp = new Exporter(game, this.testDispaly);
				exp.export();
			}
		}

		if(com.equals("addSprite"))
		{
			new SpriteAddMenu(testDispaly);
		}

		if(com.equals("addCode"))
		{
			String name = JOptionPane.showInputDialog(frame, "Script Name", "Add Script", JOptionPane.PLAIN_MESSAGE);

			if(name != null && !name.isEmpty())
			{
				File script = new File(testDispaly.getWorkspace(), "Src/code/game/" + name + ".java");

				try
				{
					script.createNewFile();
				}catch(Exception e) {}

				//open file with default text editor

				BasicWriter writer = new BasicWriter(new FileAPI(script));

				writer.writeln("package code.game;");
				writer.writeln("");
				writer.writeln("import call.gamerunner.main.*;");
				writer.writeln("import call.game.input.keyboard.*;");
				writer.writeln("import call.game.input.mouse.*;");
				writer.writeln("import call.game.input.*;");
				writer.writeln("");
				writer.writeln("public class " + name);
				writer.writeln("{");
				writer.writeln("");
				writer.writeln("}");

				writer.finish();

				try
				{
					Desktop.getDesktop().open(script);
				}catch(IOException e) {e.printStackTrace();}
			}
		}
		
		
		if(com.equals("EditKey"))
			new KeyBindEditMenu(this);
		

		if(com.equals("addEntity"))
		{
			new EntityAddMenu(this.testDispaly);
		}

		if(com.equals("addVar"))
		{
			new VarAddMenu(this.testDispaly);
		}

		if(com.equals("new"))
		{
			testDispaly.cleenup();
			
			JFileChooser browse = new JFileChooser(new File("."));

			browse.setMultiSelectionEnabled(false);
			browse.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int i = browse.showOpenDialog(frame);

			if(i == JFileChooser.APPROVE_OPTION)
			{

				File dir = browse.getSelectedFile();

				//setup structure

				File src = new File(dir, "Src");
				File code = new File(src, "code");
				File game = new File(code, "game");

				File sprites = new File(dir, "Sprites");
				File spriteData = new File(sprites, "Data.call");

				File entitys = new File(dir, "Entitys");
				File entityData = new File(entitys, "Data.call");

				File data = new File(dir, "Data");
				File varData = new File(data, "Vars.call");

				try
				{
					src.mkdir();
					code.mkdir();
					game.mkdir();

					sprites.mkdir();
					spriteData.createNewFile();

					entitys.mkdir();
					entityData.createNewFile();

					data.mkdir();
					varData.createNewFile();
				}catch(Exception e) {e.printStackTrace();}

				CFile sprite = new CFile(spriteData);

				Element e = new Element("NOOP");

				e.addValue(new Value("NOOP", "1"));

				sprite.addElement(e);

				sprite.save();

				CFile entity = new CFile(entityData);

				Element e1 = new Element("NOOP");

				e1.addValue(new Value("NOOP", "1"));

				entity.addElement(e1);

				entity.save();

				CFile data_ = new CFile(varData);

				Element e2 = new Element("NOOP");

				e2.addValue(new Value("NOOP", "1"));

				data_.addElement(e2);

				data_.save();

				testDispaly.setWorkspace(dir);

				export.setEnabled(true);
				publish.setEnabled(true);

				add.setEnabled(true);
				
				engine.setEnabled(true);
			}
		}

		if(com.equals("open"))
			openGame();

		if(com.equals("exit"))
			System.exit(0);


		// set checkbox values

		testDispaly.setViewPrefabs(prefabs.isSelected());
		testDispaly.setViewAnimations(animation.isSelected());
		testDispaly.setViewWireframe(wireframe.isSelected());
	}
	
	
	public void openGame()
	{
		testDispaly.cleenup();
		
		JFileChooser browse = new JFileChooser(new File("."));

		browse.setMultiSelectionEnabled(false);
		browse.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int i = browse.showOpenDialog(frame);

		if(i == JFileChooser.APPROVE_OPTION)
		{
			File dir = browse.getSelectedFile();

			testDispaly.setWorkspace(dir);

			// load sprites

			File spriteData = new File(dir, "Sprites/Data.call");

			CFile cf = new CFile(spriteData);

			for(Element e : cf.getElements())
			{
				if(e.getName().equals("Sprite"))
				{
					int x = e.getValue("X").getInt(0);
					int y = e.getValue("Y").getInt(0);

					String imageS = e.getValue("Image").getValue();
					String name = e.getValue("Name").getValue();

					BufferedImage image = null;

					try
					{
						image = ImageIO.read(new File(dir, "Sprites/" + imageS));
					}catch(Exception ee) {}

					Image img = new Image(image);

					boolean prefab = e.getValue("Prefab").getBoolean(false);

					Sprite s = new Sprite(x, y, img);

					SpriteWrapper sw = new SpriteWrapper(s, prefab, imageS, name);

					testDispaly.addSprite(sw);
				}
			}

			File entityData = new File(dir, "Entitys/Data.call");
			cf = new CFile(entityData);

			for(Element e : cf.getElements())
			{
				if(e.getName().equals("Entity"))
				{
					int x = e.getValue("X").getInt(0);
					int y = e.getValue("Y").getInt(0);
					boolean animation = e.getValue("Animation").getBoolean(false);
					int id = e.getValue("ID").getInt(0);
					
					String imageS = e.getValue("Image").getValue();
					String name = e.getValue("Name").getValue();
					
					String tag = e.getValue("Tag").getValue();

					Sprite s = null;

					if(!animation)
					{
						try
						{
							BufferedImage image = null;

							image = ImageIO.read(new File(dir, "Entitys/" + imageS));

							Image img = new Image(image);

							s = new Sprite(x, y, img);

						}catch(Exception ee) {ee.printStackTrace();}
					}
					else
					{
						Animation ani = AnimationIO.loadAnimation(new File(dir, "Entitys/" + imageS));

						s = new AnimatedSprite(x, y, ani);
					}

					boolean prefab = e.getValue("Prefab").getBoolean(false);

					EntityWrapper sw = new EntityWrapper(new BasicEntity(s, id), prefab, imageS, name, tag, id, s instanceof AnimatedSprite);

					testDispaly.addEntity(sw);
				}
			}

			// add scripts to text table
			
			
			export.setEnabled(true);
			publish.setEnabled(true);
			
			add.setEnabled(true);
			
			engine.setEnabled(true);
		}
	}
}
