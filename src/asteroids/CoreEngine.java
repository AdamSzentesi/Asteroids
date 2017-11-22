package asteroids;

import asteroids.games.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoreEngine
{
	private final float delta;
	private boolean running;
	private Game game;
	private final long BILLION = 1000000000;
	
	public CoreEngine(int width, int height, int FPS, String title)
	{
		this.delta = (float) 1 / FPS;
		this.running = false;
		this.game = new Testgame(width, height);
		
		Window.create(width, height, title);
	}
	
	public void start()
	{
		System.out.println("CoreEngine: started");
		if(!this.running)
		{
			run();
		}
	}
	
	public void stop()
	{
		System.out.println("CoreEngine: stopped");
		if(this.running)
		{
			this.running = false;
		}
	}
	
	private void run()
	{
		System.out.println("CoreEngine: running");
		this.running = true;
		game.initialize();
		
		long frameCounter = 0;
		int frames = 0;
		
		long lastCycleDelta;
		long unprocessedTime = 0;
		boolean canRender;
		long updateDelta = 0;
		
		long startTime;
		long endTime = Time.getTime();
		
		
		while(this.running)
		{
			canRender = false;
			
			startTime = Time.getTime();
			lastCycleDelta = startTime - endTime;
			endTime = startTime;
			
			unprocessedTime += lastCycleDelta;
			frameCounter += lastCycleDelta;
			
			while(unprocessedTime > this.delta * BILLION)
			{
				long updateStart = Time.getTime();
				canRender = true;
				unprocessedTime -= Math.max(this.delta * BILLION, updateDelta);
				
				if(Window.isCloseRequested())
				{
					stop();
					break;
				}
					
				this.game.update(this.delta);
				if(frameCounter >= BILLION)
				{
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
				updateDelta = Time.getTime() - updateStart;
			}
			
			if(canRender)
			{
				this.game.render();
				Window.render();
				frames++;
			}
			else
			{
				sleep(1);
			}
		}
		
		cleanUp();
	}
	
	/**
	 * Clean-up before exit
	 */
	private void cleanUp()
	{
		game.cleanUp();
		Window.destroy();
	}
	
	private void sleep(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException ex)
		{
			Logger.getLogger(CoreEngine.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
}