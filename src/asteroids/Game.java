package asteroids;

abstract public class Game
{
	private StateManager stateManager;
	
	public Game(int width, int height)
	{
		this.stateManager = new StateManager();
	}
	
	public final void pushState(State state)
	{
		this.stateManager.push(state);
	}
	
	public final void popState()
	{
		this.stateManager.pop();
	}
	
	public final void setState(State state)
	{
		this.popState();
		this.pushState(state);
	}
	
	public final void initialize()
	{
		this.stateManager.initialize();
	}
	
	public final void update(float delta)
	{
		this.stateManager.update(delta);
	}
	
	public final void render()
	{
		this.stateManager.render();
	}
	
	public final void cleanUp()
	{
		this.stateManager.cleanUp();
	}

}
