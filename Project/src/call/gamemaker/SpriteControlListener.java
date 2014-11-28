package call.gamemaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import call.game.image.Sprite;
import call.game.input.mouse.Mouse;
import call.game.input.mouse.MouseListener;
import call.game.physicx.bounding.BoundingBox;
import call.gamemaker.ui.DisplayComponent;

public class SpriteControlListener implements MouseListener, ActionListener
{
	private MakerFrame frame;

	private Sprite selected;
	private int selectedIndex = -100;
	private boolean dragging;

	public SpriteControlListener(MakerFrame frame)
	{
		this.frame = frame;
	}

	@Override
	public void onClick(int x, int y, int state, int button)
	{
		if(button == 1 && state == Mouse.STATE_MOUSE_DOWN)
		{
			x -= 3;
			y -= 50;

			if(selected != null)
			{
				selected = null;
				dragging = false;
				selectedIndex = -100;
				return;
			}

			for(int i = 0; i < frame.testDispaly.getSprites().size(); i++)
			{
				SpriteWrapper sw = frame.testDispaly.getSprites().get(i);

				Sprite s = sw.getSprite();

				BoundingBox b = s.getBounds();

				if(x > b.getX() && x < b.getX() + b.getWidth() && y > b.getY() && y < b.getY() + b.getHeight())
				{
					selected = s;
					selectedIndex = i;

					dragging = true;
				}
			}
		}

		if(button == 3 && state == Mouse.STATE_MOUSE_DOWN)
		{
			x -= 3;
			y -= 50;

			if(selected != null)
			{
				selected = null;
				dragging = false;
				selectedIndex = -100;
				return;
			}

			for(int i = 0; i < frame.testDispaly.getSprites().size(); i++)
			{
				SpriteWrapper sw = frame.testDispaly.getSprites().get(i);

				Sprite s = sw.getSprite();

				BoundingBox b = s.getBounds();

				if(x > b.getX() && x < b.getX() + b.getWidth() && y > b.getY() && y < b.getY() + b.getHeight())
				{
					selected = s;
					selectedIndex = i;

					dragging = false;
				}
			}

			if(selected != null)
			{
				JPopupMenu popup = new JPopupMenu();

				JMenuItem edit = new JMenuItem("Edit");
				edit.setActionCommand("edit");
				edit.addActionListener(this);
				popup.add(edit);

				popup.addSeparator();
				JMenuItem remove = new JMenuItem("Delete");
				remove.setActionCommand("rem");
				remove.addActionListener(this);
				popup.add(remove);

				popup.show(frame.testDispaly, x, y);
			}
		}
	}

	@Override
	public void onMove(int x, int y)
	{
		if(selected != null && dragging && selectedIndex != -100)
		{
			selected.setX(x - 3 - selected.getImage().getWidth() / 2);
			selected.setY(y - 50 - selected.getImage().getHeight() / 2);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		String command = ae.getActionCommand();

		if(selectedIndex != -100)
		{
			if(command.equals("rem"))
			{
				DisplayComponent dc = frame.testDispaly;

				System.out.println(selectedIndex);

				dc.getSprites().remove(selectedIndex);
			}
		}
	}
}
