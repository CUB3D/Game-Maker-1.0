package call.gamemaker;

public class KeyBindWrapper
{
	private String name;
	private int realKey;
	
	public KeyBindWrapper(String name, int realKey)
	{
		this.name = name;
		this.realKey = realKey;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getRealKey()
	{
		return realKey;
	}
}
