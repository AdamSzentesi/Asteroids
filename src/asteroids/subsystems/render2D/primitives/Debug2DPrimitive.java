package asteroids.subsystems.render2D.primitives;

import asteroids.Util;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL15.*;

public class Debug2DPrimitive
{
	public int vbo;
	public int vboCount;
	public int ibo;
	public int iboCount;
	
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
