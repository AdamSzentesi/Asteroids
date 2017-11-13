package asteroids.components.Geometry2D;

import asteroids.Util;
import asteroids.components.Component;
import asteroids.math.Vector2f;
import asteroids.math.Vector3f;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL15.*;

public class Render2DLineComponent extends Component
{
	public static int defaultVbo;
	public static int defaultVboCount;
	public static int defaultIbo;
	public static int defaultIboCount;
	
	public int vbo;
	public int vboCount;
	public int ibo;
	public int iboCount;
	private static boolean initialized;
	
	public Vector3f color = new Vector3f(1.0f, 1.0f, 1.0f);
	
	public Render2DLineComponent()
	{
		if(!initialized)
		{
			//vertex data
			float[] vertexArray =
			{
				-0.1f, 0.05f,
				0.0f, 0.2f,
				0.0f, 0.0f,
				0.1f, 0.05f,
			};
			//index data
			int[] indexArray =
			{
				0, 1, 3, 2, 0
			};

			//turn vertex array to buffer
			FloatBuffer vertexBuffer = Util.makeFlippedBuffer(vertexArray);
			defaultVbo = makeVBO(vertexBuffer);
			defaultVboCount = vertexArray.length; 

			//turn index array to buffer
			IntBuffer indexBuffer = Util.makeFlippedBuffer(indexArray);
			defaultIbo = makeIBO(indexBuffer);
			defaultIboCount = indexArray.length;		
			initialized = true;
		}
		
		this.vbo = this.defaultVbo;
		this.vboCount = this.defaultVboCount;
		this.ibo = this.defaultIbo;
		this.iboCount = this.defaultIboCount;
	}
	
	public void loadLine(float[] vertexArray, int[] indexArray)
	{
		//turn vertex array to buffer
		FloatBuffer vertexBuffer = Util.makeFlippedBuffer(vertexArray);
		vbo = makeVBO(vertexBuffer);
		vboCount = vertexArray.length; 
		
		//turn index array to buffer
		IntBuffer indexBuffer = Util.makeFlippedBuffer(indexArray);
		ibo = makeIBO(indexBuffer);
		iboCount = indexArray.length;
	}
	
	public Render2DLineComponent(Vector2f a, Vector2f b)
	{
		//vertex data
		float[] vertexArray =
		{
			a.x, a.y, b.x, b.y
		};
		//index data
		int[] indexArray =
		{
			0, 1
		};
		
		//turn vertex array to buffer
		FloatBuffer vertexBuffer = Util.makeFlippedBuffer(vertexArray);
		vbo = makeVBO(vertexBuffer);
		vboCount = vertexArray.length; 
		
		//turn index array to buffer
		IntBuffer indexBuffer = Util.makeFlippedBuffer(indexArray);
		ibo = makeIBO(indexBuffer);
		iboCount = indexArray.length;
	}
	
	//return VBO from FloatBuffer
	private int makeVBO(FloatBuffer inputData)
	{
		//create VBO
		int vertexBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
			glBufferData(GL_ARRAY_BUFFER, inputData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return vertexBuffer;
	}
	
	//return IBO from FloatBuffer
	private int makeIBO(IntBuffer inputData)
	{
		//create IBO
		int indexBuffer = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, inputData, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		return indexBuffer;
	}
	
	@Override
	public void finalize()
	{
		glDeleteBuffers(this.vbo);
		glDeleteBuffers(this.ibo);
	}
}
