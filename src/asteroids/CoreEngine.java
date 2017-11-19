package asteroids;

import asteroids.games.Testgame;
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
		
		double lastCycleDelta;
		double unprocessedTime = 0;
		boolean canRender;
		double updateDelta = 0;
		
		double startTime;
		double endTime = Time.getTime();
		
		
		while(this.running)
		{
			canRender = false;
			
			startTime = Time.getTime();
			lastCycleDelta = startTime - endTime;
			endTime = startTime;
			
			unprocessedTime += lastCycleDelta;
			
			while(unprocessedTime > this.delta * BILLION)
			{
				double updateStart = Time.getTime();
				canRender = true;
				unprocessedTime -= Math.max(this.delta * BILLION, updateDelta);
				
				if(Window.isCloseRequested())
				{
					stop();
				}
					
				this.game.update(this.delta);
				updateDelta = Time.getTime() - updateStart;
			}
			
			if(canRender)
			{
				this.game.render();
				Window.render();
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


//package asteroids;
//
//import asteroids.games.Testgame;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class CoreEngine
//{
//	private int width;
//	private int height;
//	private double minFrameTime;
//	private Game game;
//	private boolean running;
//	
//	/**
//	 * Core engine constructor
//	 * 
//	 * @param width window width in px
//	 * @param height window height in px
//	 * @param fps FPS cap
//	 * @param title window title
//	 */
//	public CoreEngine(int width, int height, int fps, String title)
//	{
//		System.out.println("CoreEngine initiated");
//		this.running = false;
//		this.game = new Testgame(width, height);
//		this.width = width;
//		this.height = height;
//		this.minFrameTime = 1.0d / fps;
//		
//		Window.create(this.width, this.height, title);
//	}
//	
//	/**
//	 * Start the core engine
//	 */
//	public void start()
//	{
//		if(!running)
//		{
//			run();
//		}
//	}
//	
//	/**
//	 * Stop the core engine
//	 */
//	public void stop()
//	{
//		if(running)
//		{
//			running = false;
//		}
//	}
//	
//	/**
//	 * Main running cycle
//	 */
//	private void run()
//	{
//		System.out.println("CoreEngine running");
//		
//		running = true;
//		game.initialize();
//		
//		int frames = 0;
//		double frameCounter = 0;
//		double unprocessedTime = 0;
//		double lastTime = Time.getTime();
//
//		
//		System.out.println("---------------");
//		
//		while(running)
//		{
//			boolean render = false;
//			
//			double startTime = Time.getTime();
//			double passedTime = startTime - lastTime;
//			
//			lastTime = startTime;
//			unprocessedTime += passedTime;
//			frameCounter += passedTime;
//			
//			while(unprocessedTime > minFrameTime)
//			{
//				render = true;
//				
//				unprocessedTime -= minFrameTime;
//				
//				if(Window.isCloseRequested())
//				{
//					stop();
//				}
//
//				long sTime = System.nanoTime();
//				game.update((float)minFrameTime);
//				Window.render();
//				long eTime = System.nanoTime();
////				System.out.println("--- update time: " + (float)(eTime - sTime)/1000000 + "ms");
//				
//				if(frameCounter >= 1.0)
//				{
////					System.out.println("FPS: " + frames);
//					frames = 0;
//					frameCounter = 0;
//				}
//			}
//
//			if(render)
//			{
//				//Window.render();
//				frames++;
//			}
//			else
//			{
//				try
//				{
//					Thread.sleep(5);
//				}
//				catch (InterruptedException ex)
//				{
//					Logger.getLogger(CoreEngine.class.getName()).log(Level.SEVERE, null, ex);
//				}
//			}
//		}
//		
//		cleanUp();
//	}
//	
//	/**
//	 * Clean-up before exit
//	 */
//	private void cleanUp()
//	{
//		game.cleanUp();
//		Window.destroy();
//	}
//
//}
