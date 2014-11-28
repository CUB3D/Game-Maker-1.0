package call.gamemaker;

import call.game.image.Sprite;

public class SpriteWrapper
{
	private Sprite sprite;
	private boolean prefab;
	private String image;
	private String name;
	
	public SpriteWrapper(Sprite s, boolean prefab, String image, String name)
	{
		this.sprite = s;
		this.prefab = prefab;
		this.name = name;
		this.image = image;
	}
	
	public Sprite getSprite()
	{
		return sprite;
	}
	
	public boolean isPrefab()
	{
		return prefab;
	}
	
	public String getImage()
	{
		return image;
	}
	
	public String getName()
	{
		return name;
	}
}
