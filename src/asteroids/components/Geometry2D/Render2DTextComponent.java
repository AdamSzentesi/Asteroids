package asteroids.components.Geometry2D;

import asteroids.Util;
import asteroids.components.Component;
import asteroids.math.Vector3f;
import asteroids.subsystems.render3D.OpenGLUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL15.*;

public class Render2DTextComponent extends Component
{
	private static boolean initialized;
	
	public static int vbo;
	public static int ibo;
	public static int iboCount;
	
	public int x;
	public int y;
	public Vector3f color;
	public String string;
	public int width;
	public int height;
	
	public Render2DTextComponent()
	{
		if(!initialized)
		{
			//vertex data
			float[] vertexArray =
			{
				-0.5f, 0.5f, 0.0f, 0.0f,
				0.5f, 0.5f, 0.0625f, 0.0f,
				0.5f, -0.5f, 0.0625f, 0.0625f,
				-0.5f, -0.5f, 0.0f, 0.0625f,
			};
			//index data
			int[] indexArray =
			{
				0, 1, 2, 3, 0, 2
			};

			//turn vertex array to buffer
			FloatBuffer vertexBuffer = Util.makeFlippedBuffer(vertexArray);
			this.vbo = OpenGLUtils.makeVBO(vertexBuffer);

			//turn index array to buffer
			IntBuffer indexBuffer = Util.makeFlippedBuffer(indexArray);
			this.ibo = OpenGLUtils.makeIBO(indexBuffer);
			this.iboCount = indexArray.length;		
			initialized = true;
		}
		this.x = 0;
		this.y = 0;
		this.color = new Vector3f(1.0f, 1.0f, 1.0f);
		this.string = "DUMMY";
		this.width = 64;
		this.height = 64;
	}
	
	@Override
	public void finalize()
	{
		glDeleteBuffers(this.vbo);
		glDeleteBuffers(this.ibo);
	}

	public void setString(String string)
	{
		this.string = string;
	}

	public String get()
	{
		return this.string;
	}
	
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
}
