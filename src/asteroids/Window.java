package asteroids;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class Window
{
	public static void create(int width, int height, String title)
	{
		showDisplayModes();
		
		try
		{
			Display.setTitle(title);
			Display.setDisplayMode(findDisplayMode(width, height, 32));
			//Display.setFullscreen(true);
			Display.create(new PixelFormat());
			
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}
		System.out.println("OpenGL - window created: " + width + "x" + height);
	}
	
	private static DisplayMode findDisplayMode(int width, int height, int bpp) throws LWJGLException
	{
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		for ( DisplayMode mode : modes )
		{
			if ( mode.getWidth() == width && mode.getHeight() == height && mode.getBitsPerPixel() >= bpp )
			return mode;
		}
		return null;
	}
	
	public static void destroy()
	{
		Display.destroy();
	}
	
	public static boolean isCloseRequested()
	{
		return Display.isCloseRequested();
	}

	static void sync(int fps)
	{
		Display.sync(fps);
	}

	public static void render()
	{
		Display.update();
	}
	
	public static int getWidth()
	{
		return Display.getDisplayMode().getWidth();
	}
	
	public static int getHeight()
	{
		return Display.getDisplayMode().getHeight();
	}

	static int fps()
	{
		return 666;
	}
	
	public static void showDisplayModes()
	{
		DisplayMode[] modes;
		try
		{
			modes = Display.getAvailableDisplayModes();
			for (int i=0;i<modes.length;i++)
			{
				DisplayMode current = modes[i];
				System.out.println(current.getWidth() + "x" + current.getHeight() + "x" + current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
			}
		}
		catch (LWJGLException ex)
		{
			Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
}
