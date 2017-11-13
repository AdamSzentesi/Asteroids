package asteroids.subsystems.render3D.primitives;

import asteroids.Util;
import asteroids.math.Vector3f;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL15.*;

public class Debug3DLinePrimitive
{
	public int vbo;
	public int vboCount;
	public int ibo;
	public int iboCount;
	
	public Debug3DLinePrimitive(Vector3f start, Vector3f end)
	{
		float[] vertexArray = new float[6];
		vertexArray[0] = start.x;
		vertexArray[1] = start.y;
		vertexArray[2] = start.z;
		vertexArray[3] = end.x;
		vertexArray[4] = end.y;
		vertexArray[5] = end.z;

		int[] indexArray = {0, 1};
		
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

	public void destroy()
	{
		glDeleteBuffers(this.vbo);
		glDeleteBuffers(this.ibo);
	}
}
