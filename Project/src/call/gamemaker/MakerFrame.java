package call.gamemaker;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

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
import call.game.main.Unknown;
import call.gamemaker.tasks.ExportTask;
import call.gamemaker.tasks.OpenTask;
import call.gamemaker.tasks.PublishTask;
import call.gamemaker.tasks.SaveTask;
import call.gamemaker.ui.DisplayComponent;
import call.gamemaker.ui.EntityAddMenu;
import call.gamemaker.ui.KeyBindEditMenu;
import call.gamemaker.ui.SpriteAddMenu;
import call.gamemaker.ui.TextTable;
import call.gamemaker.ui.VarAddMenu;

public class MakerFrame
{
	public static JFrame frame;
	public DisplayComponent testDisplay;

	public JMenu file;

	public JMenuItem ne;
	public JMenuItem open;
	public JMenuItem save;
	public JMenuItem export;
	public JMenuItem publish;
	public JMenuItem exit;

	public JMenu add;

	public JMenu view;

	public JMenu engine;

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
		ne.addActionListener(e -> newProject());
		file.add(ne);

		open = new JMenuItem("Open");
		open.addActionListener(e -> new OpenTask(testDisplay, this).excecute());
		file.add(open);

		save = new JMenuItem("Save");
		save.addActionListener(e -> new SaveTask(testDisplay).excecute());
		file.add(save);

		file.addSeparator();

		export = new JMenuItem("Export");
		export.addActionListener(e -> new ExportTask(testDisplay).excecute());
		export.setEnabled(false);
		file.add(export);

		publish = new JMenuItem("Publish");
		publish.addActionListener(e -> new PublishTask(testDisplay).excecute());
		publish.setEnabled(false);
		file.add(publish);

		file.addSeparator();

		exit = new JMenuItem("Quit");
		exit.addActionListener(e -> System.exit(0));
		file.add(exit);


		add = new JMenu("Add");
		add.setEnabled(false);
		bar.add(add);

		JMenuItem sprite = new JMenuItem("Sprite");
		sprite.addActionListener(e -> new SpriteAddMenu(testDisplay));
		add.add(sprite);

		JMenuItem code = new JMenuItem("Script");
		code.addActionListener(e -> addcode());
		add.add(code);

		JMenuItem entity = new JMenuItem("Entity");
		entity.addActionListener(e -> new EntityAddMenu(this.testDisplay));
		add.add(entity);

		JMenuItem varible = new JMenuItem("Global var");
		varible.addActionListener(e -> new VarAddMenu(this.testDisplay));
		add.add(varible);


		engine = new JMenu("Engine");
		engine.setEnabled(false);
		bar.add(engine);

		JMenuItem editKey = new JMenuItem("Edit KeyBinds");
		editKey.addActionListener(e -> new KeyBindEditMenu(this));
		engine.add(editKey);


		view = new JMenu("View");
		bar.add(view);

		prefabs = new JCheckBoxMenuItem("Prefabs");
		prefabs.setSelected(true);
		view.add(prefabs);

		animation = new JCheckBoxMenuItem("Animations");
		animation.setSelected(true);
		view.add(animation);

		wireframe = new JCheckBoxMenuItem("Wire frame");
		wireframe.setSelected(true);
		view.add(wireframe);

		frame.setJMenuBar(bar);

		testDisplay = new DisplayComponent(this);

		testDisplay.setBounds(0, 0, 800, frame.getHeight());

		table = new TextTable();

		table.setBounds(800, 0, frame.getWidth() - 800, frame.getHeight());

		frame.add(table);


		frame.add(testDisplay);

		frame.setVisible(true);

		while(true)
		{
			frame.repaint();
			
			update();
		}
	}
	
	public void update()
	{
		testDisplay.setViewPrefabs(prefabs.isSelected());
		testDisplay.setViewAnimations(animation.isSelected());
		testDisplay.setViewWireframe(wireframe.isSelected());
	}
	
	public void addcode()
	{
		String name = JOptionPane.showInputDialog(frame, "Script Name", "Add Script", JOptionPane.PLAIN_MESSAGE);

		if(name != null && !name.isEmpty())
		{
			File script = new File(testDisplay.getWorkspace(), "Src/code/game/" + name + ".java");

			try
			{
				script.createNewFile();
			}catch(Exception e) {e.printStackTrace();}

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

	public void newProject()
	{
		testDisplay.cleanup();

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

			testDisplay.setWorkspace(dir);

			export.setEnabled(true);
			publish.setEnabled(true);

			add.setEnabled(true);

			engine.setEnabled(true);
		}
	}
	
	public static void main(String[] args)
	{
		new MakerFrame();
	}
}
