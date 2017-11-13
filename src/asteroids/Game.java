package asteroids;

abstract public class Game
{
	private int width;
	private int height;
	
	public Game(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	abstract public void initialize();
	abstract public void update(float delta);
	abstract public void cleanUp();
}
