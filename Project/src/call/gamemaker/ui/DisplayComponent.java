package call.gamemaker.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import call.game.entitys.BasicEntity;
import call.game.image.AnimatedSprite;
import call.game.image.Sprite;
import call.game.main.Unknown;
import call.gamemaker.EntityWrapper;
import call.gamemaker.KeyBindWrapper;
import call.gamemaker.MakerFrame;
import call.gamemaker.SpriteWrapper;
import call.utils.StringUtils;

public class DisplayComponent extends Component
{
	private static final long serialVersionUID = 414829967024319118L;

	private List<SpriteWrapper> sprites = new ArrayList<SpriteWrapper>();
	private List<EntityWrapper> entitys = new ArrayList<EntityWrapper>();
	
	private List<KeyBindWrapper> keybinds = new ArrayList<KeyBindWrapper>();

	private File workspace;
	
	private MakerFrame base;

	private boolean viewPrefabs = true;
	private boolean viewAnimation = true;
	private boolean viewWireframe = true;
	
	public DisplayComponent(MakerFrame base)
	{
		this.base = base;
	}
	

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		Unknown.graphics = g;

		g.fillRect(getX(), getY(), getWidth(), getHeight());

		for(SpriteWrapper sw : sprites)
		{
			Sprite s = sw.getSprite();

			if(sw.isPrefab())
			{
				if(viewPrefabs)
				{
					s.render();

					g.setColor(Color.BLACK);
					float height = 16;
					g.setFont(g.getFont().deriveFont(height));
					String ss = "P";
					g.drawString(ss, s.getX() + s.getImage().getWidth() / 2 - StringUtils.getStringLengthInPixels(ss, g) / 2, s.getY() + s.getImage().getHeight() / 2 + (int) height / 2);

					drawMesh(s, g);
				}
			}
			else
			{
				s.render();

				drawMesh(s, g);
			}

		}

		for(int i = 0; i < entitys.size(); i++)
		{
			EntityWrapper sw = entitys.get(i);
			
			BasicEntity s = sw.getEntity();


			if(sw.isPrefab())
			{
				if(viewPrefabs)
				{
					s.render();

					if(s.getSprite() instanceof AnimatedSprite)
						if(viewAnimation)
							((AnimatedSprite) s.getSprite()).advance();

					g.setColor(Color.BLACK);
					float height = 16;
					g.setFont(g.getFont().deriveFont(height));
					String ss = "EP";
					g.drawString(ss, s.getX() + s.getSprite().getImage().getWidth() / 2 - StringUtils.getStringLengthInPixels(ss, g) / 2, s.getY() + s.getSprite().getImage().getHeight() / 2 + (int) height / 2);

					drawMesh(s.getSprite(), g);
				}
			}
			else
			{
				s.render();

				if(s.getSprite() instanceof AnimatedSprite)
					if(viewAnimation)
						((AnimatedSprite) s.getSprite()).advance();

				g.setColor(Color.BLACK);
				float height = 16;
				g.setFont(g.getFont().deriveFont(height));
				String ss = "E";
				g.drawString(ss, s.getX() + s.getSprite().getImage().getWidth() / 2 - StringUtils.getStringLengthInPixels(ss, g) / 2, s.getY() + s.getSprite().getImage().getHeight() / 2 + (int) height / 2);

				drawMesh(s.getSprite(), g);
			}
		}
	}

	public void drawMesh(Sprite img, Graphics g)
	{
		if(!viewWireframe)
			return;


		g.setColor(Color.RED);

		int x = img.getX();
		int y = img.getY();
		int width = img.getImage().getWidth();
		int height = img.getImage().getHeight();

		g.drawLine(x, y, x, y + height);
		g.drawLine(x, y + height, x + width, y + height);
		g.drawLine(x + width, y + height, x + width, y);
		g.drawLine(x + width, y, x, y);

		g.drawLine(x, y, x + width, y + height);
	}

	public void addSprite(SpriteWrapper s)
	{
		sprites.add(s);
		
		base.table.addString("Sprite: " + s.getName());
	}
	public List<SpriteWrapper> getSprites()
	{
		return sprites;
	}

	public void addEntity(EntityWrapper e)
	{
		this.entitys.add(e);
		
		base.table.addString("Entity: " + e.getName());
	}

	public List<EntityWrapper> getEntitys()
	{
		return entitys;
	}

	public void addKeyBind(KeyBindWrapper kbw)
	{
		keybinds.add(kbw);
	}
	
	public List<KeyBindWrapper> getKeybinds()
	{
		return keybinds;
	}
	
	public void setWorkspace(File workspace)
	{
		this.workspace = workspace;
	}

	public File getWorkspace()
	{
		return workspace;
	}

	public void setViewPrefabs(boolean viewPrefabs)
	{
		this.viewPrefabs = viewPrefabs;
	}

	public void setViewAnimations(boolean selected)
	{
		this.viewAnimation = selected;
	}

	public void setViewWireframe(boolean selected)
	{
		this.viewWireframe = selected;
	}
	
	public void cleanup()
	{
		this.entitys.clear();
		this.sprites.clear();
		this.keybinds.clear();
		this.workspace = null;
	}
}
