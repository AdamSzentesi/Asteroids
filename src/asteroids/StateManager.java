package asteroids;

import java.util.Stack;

/**
 *
 * @author Adam Szentesi
 */
public class StateManager
{
	private Stack<State> states;
	
	public StateManager()
	{
		this.states = new Stack();
	}
	
	public void push(State state)
	{
		this.states.push(state);
		this.states.peek().setStateManager(this);
	}
	
	public void pop()
	{
		this.states.pop();
	}
	
	public void set(State state)
	{
		this.pop();
		this.push(state);
	}
	
	public void initialize()
	{
		this.states.peek().initialize();
	}
	
	public void update(float delta)
	{
		this.states.peek().update(delta);
	}
	
	public void render()
	{
		this.states.peek().render();
	}
	
	public void cleanUp()
	{
		this.states.peek().render();
	}
}
