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

public class Debug3DSpherePrimitive
{
	public int vbo;
	public int vboCount;
	public int ibo;
	public int iboCount;
	
	public Debug3DSpherePrimitive()
	{
		float[] vertexArray =
		{
			-1.000000f, -0.000000f, -0.000000f,
			-0.923880f, 0.382683f, -0.000000f,
			-0.707107f, 0.707107f, -0.000000f,
			-0.382683f, 0.923880f, -0.000000f,
			-0.000000f, 1.000000f, -0.000000f,
			0.382683f, 0.923880f, 0.000000f,
			0.707107f, 0.707107f, 0.000000f,
			0.923880f, 0.382684f, 0.000000f,
			1.000000f, 0.000000f, 0.000000f,
			0.923880f, -0.382683f, 0.000000f,
			0.707107f, -0.707107f, 0.000000f,
			0.382684f, -0.923880f, 0.000000f,
			0.000000f, -1.000000f, -0.000000f,
			-0.382684f, -0.923879f, -0.000000f,
			-0.707107f, -0.707107f, -0.000000f,
			-0.923880f, -0.382683f, -0.000000f,

			0.000000f, 0.000000f, -1.000000f,
			0.000000f, 0.382683f, -0.923880f,
			-0.000000f, 0.707107f, -0.707107f,
			-0.000000f, 0.923880f, -0.382683f,
			-0.000000f, 1.000000f, 0.000000f,
			-0.000000f, 0.923880f, 0.382684f,
			-0.000000f, 0.707107f, 0.707107f,
			-0.000000f, 0.382683f, 0.923880f,
			-0.000000f, -0.000000f, 1.000000f,
			-0.000000f, -0.382683f, 0.923880f,
			0.000000f, -0.707107f, 0.707107f,
			0.000000f, -0.923880f, 0.382684f,
			0.000000f, -1.000000f, -0.000000f,
			0.000000f, -0.923879f, -0.382684f,
			0.000000f, -0.707107f, -0.707107f,
			0.000000f, -0.382683f, -0.923880f,

			0.000000f, 0.000000f, -1.000000f,
			-0.382683f, 0.000000f, -0.923880f,
			-0.707107f, 0.000000f, -0.707107f,
			-0.923880f, 0.000000f, -0.382683f,
			-1.000000f, 0.000000f, 0.000000f,
			-0.923880f, 0.000000f, 0.382684f,
			-0.707107f, 0.000000f, 0.707107f,
			-0.382683f, 0.000000f, 0.923880f,
			0.000000f, 0.000000f, 1.000000f,
			0.382683f, 0.000000f, 0.923880f,
			0.707107f, 0.000000f, 0.707107f,
			0.923880f, 0.000000f, 0.382684f,
			1.000000f, 0.000000f, -0.000000f,
			0.923879f, 0.000000f, -0.382684f,
			0.707107f, 0.000000f, -0.707107f,
			0.382683f, 0.000000f, -0.923880f,
		};
		//default index data
		int[] indexArray =
		{
			1, 0,
			2, 1,
			3, 2,
			4, 3,
			5, 4,
			6, 5,
			7, 6,
			8, 7,
			9, 8,
			10, 9,
			11, 10,
			12, 11,
			13, 12,
			14, 13,
			15, 14,
			0, 15,

			17, 16,			
			18, 17,
			19, 18,
			20, 19,
			21, 20,
			22, 21,
			23, 22,
			24, 23,
			25, 24,
			26, 25,
			27, 26,
			28, 27,
			29, 28,
			30, 29,
			31, 30,
			16, 31,

			33, 32,
			34, 33,
			35, 34,
			36, 35,
			37, 36,
			38, 37,
			39, 38,
			40, 39,
			41, 40,
			42, 41,
			43, 42,
			44, 43,
			45, 44,
			46, 45,
			47, 46,
			32, 47,
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
