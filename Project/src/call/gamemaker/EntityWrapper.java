package call.gamemaker;

import call.game.entitys.BasicEntity;
import call.game.image.AnimatedSprite;
import call.game.image.Sprite;

public class EntityWrapper
{
	private BasicEntity entity;
	private boolean prefab;
	private String image;
	private String name;
	private String tag;
	private String ID;
	private boolean isAnimated;
	private int health;
	
	
	public EntityWrapper(Sprite s, boolean prefab, String image, String name, String tag, String id, boolean animated, int maxHealth)
	{
		this.entity = new BasicEntity(s, 0);
		this.prefab = prefab;
		this.image = image;
		this.name = name;
		this.tag = tag;
		this.ID = id;
		this.isAnimated = animated;
		this.health = maxHealth;
	}
	
	public EntityWrapper(AnimatedSprite s, boolean prefab, String image, String name, String tag, String id, boolean animated, int maxHealth)
	{
		this.entity = new BasicEntity(s, 0);
		this.prefab = prefab;
		this.image = image;
		this.name = name;
		this.tag = tag;
		this.ID = id;
		this.isAnimated = animated;
		this.health = maxHealth;
	}
	
	public BasicEntity getEntity()
	{
		return entity;
	}
	
	public void setEntitySprite(Sprite s)
	{
		entity.sprite = s;
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
	
	public String getID() 
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
	
	public int getHealth()
	{
		return health;
	}
	
	public void setHealth(int health)
	{
		this.health = health;
	}
}
