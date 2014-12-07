package call.gamemaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import call.game.entitys.BasicEntity;
import call.game.input.mouse.Mouse;
import call.game.input.mouse.MouseListener;
import call.game.physicx.bounding.BoundingBox;
import call.gamemaker.ui.DisplayComponent;
import call.gamemaker.ui.EntityEditMenu;

public class EntityControlListener implements MouseListener, ActionListener
{
	private MakerFrame frame;

	private EntityWrapper selected;
	private int selectedIndex = -100;
	private boolean dragging;

	public EntityControlListener(MakerFrame frame)
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

			for(int i = 0; i < frame.testDispaly.getEntitys().size(); i++)
			{
				EntityWrapper sw = frame.testDispaly.getEntitys().get(i);

				BasicEntity s = sw.getEntity();

				BoundingBox b = s.getSprite().getBounds();

				if(x > b.getX() && x < b.getX() + b.getWidth() && y > b.getY() && y < b.getY() + b.getHeight())
				{
					selected = sw;
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

			for(int i = 0; i < frame.testDispaly.getEntitys().size(); i++)
			{
				EntityWrapper sw = frame.testDispaly.getEntitys().get(i);

				BasicEntity s = sw.getEntity();

				BoundingBox b = s.getSprite().getBounds();

				if(x > b.getX() && x < b.getX() + b.getWidth() && y > b.getY() && y < b.getY() + b.getHeight())
				{
					selected = sw;
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
			BasicEntity e = selected.getEntity();
			
			e.getSprite().setX(x - 3 - e.getSprite().getImage().getWidth() / 2);
			e.getSprite().setY(y - 50 - e.getSprite().getImage().getHeight() / 2);
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

				dc.getEntitys().remove(selectedIndex);
			}
			
			if(command.equals("edit"))
			{
				new EntityEditMenu(frame.testDispaly, selected);
			}
		}
	}
}
