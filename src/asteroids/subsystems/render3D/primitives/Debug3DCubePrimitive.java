package asteroids.subsystems.render3D.primitives;

import asteroids.Util;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class Debug3DCubePrimitive
{
	public int vbo;
	public int vboCount;
	public int ibo;
	public int iboCount;
	
	public Debug3DCubePrimitive()
	{
		float[] vertexArray =
		{
			-0.500000f, -0.500000f, 0.500000f,
			-0.500000f, 0.500000f, 0.500000f,
			-0.500000f, 0.500000f, -0.500000f,
			-0.500000f, -0.500000f, -0.500000f,
			0.500000f, -0.500000f, 0.500000f,
			0.500000f, 0.500000f, 0.500000f,
			0.500000f, 0.500000f, -0.500000f,
			0.500000f, -0.500000f, -0.500000f,
		};
		//default index data
		int[] indexArray =
		{
			0, 1,
			1, 2,
			2, 3,
			3, 0,

			4, 5,
			5, 6,
			6, 7,
			7, 4,

			0, 4,
			1, 5,
			2, 6,
			3, 7
		};
		loadMesh(vertexArray, indexArray);	
	}
	
	public void loadMesh(float[] vertexArray, int[] indexArray)
	{
		//turn vertex array to buffer
		FloatBuffer vertexBuffer = Util.makeFlippedBuffer(vertexArray);
		this.vbo = makeVBO(vertexBuffer);
		this.vboCount = vertexArray.length; 
		//turn index array to buffer
		IntBuffer indexBuffer = Util.makeFlippedBuffer(indexArray);
		this.ibo = makeIBO(indexBuffer);
		this.iboCount = indexArray.length;
	}
	
	//return VBO from FloatBuffer
	private int makeVBO(FloatBuffer inputData)
	{
		int vertexBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
			glBufferData(GL_ARRAY_BUFFER, inputData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		return vertexBuffer;
	}
	
	//return IBO from FloatBuffer
	private int makeIBO(IntBuffer inputData)
	{
		int indexBuffer = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, inputData, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		return indexBuffer;
	}
}
