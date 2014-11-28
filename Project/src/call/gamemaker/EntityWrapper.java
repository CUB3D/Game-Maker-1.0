package call.gamemaker;

import call.game.entitys.BasicEntity;
import call.game.image.Sprite;

public class EntityWrapper
{
	private BasicEntity sprite;
	private boolean prefab;
	private String image;
	private String name;
	private String tag;
	private int ID;
	private boolean isAnimated;
	
	public EntityWrapper(BasicEntity s, boolean prefab, String image, String name, String tag, int id, boolean animated)
	{
		this.sprite = s;
		this.prefab = prefab;
		this.image = image;
		this.name = name;
		this.tag = tag;
		this.ID = id;
		this.isAnimated = animated;
	}
	
	public BasicEntity getEntity()
	{
		return sprite;
	}
	
	public void setEntitySprite(Sprite s)
	{
		sprite.sprite = s;
	}
	
	public boolean isPrefab()
	{
		return prefab;
	}
	
	public void setPrefab(boolean prefab)
	{
		this.prefab = prefab;
	}
	
	public String getImage() 
	{
		return image;
	}
	
	public void setImage(String image)
	{
		this.image = image;
	}
	
	public int getID() 
	{
		return ID;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean isAnimated()
	{
		return isAnimated;
	}
	
	public void setAnimated(boolean isAnimated)
	{
		this.isAnimated = isAnimated;
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public void setTag(String tag)
	{
		this.tag = tag;
	}
}
