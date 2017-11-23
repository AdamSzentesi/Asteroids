package asteroids;

/**
 *
 * @author Adam Szentesi
 */
public abstract class State
{
	private StateManager stateManager;
	private int width;
	private int height;
	
	public State(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
		
	public final int getWidth()
	{
		return this.width;
	}
	
	public final int getHeight()
	{
		return this.height;
	}
	
	public final void setStateManager(StateManager stateManager)
	{
		this.stateManager = stateManager;
	}
	
	public abstract void initialize();
	public abstract void update(float delta);
	public abstract void render();
	public abstract void cleanUp();
}
