package asteroids.games;

import asteroids.Game;
import asteroids.states.*;


public class Testgame extends Game
{
	public Testgame(int width, int height)
	{
		super(width, height);
		this.pushState(new GameState(width, height));
		this.pushState(new MenuState(width, height));
		this.pushState(new IntroState(width, height));
		
	}
	
}
