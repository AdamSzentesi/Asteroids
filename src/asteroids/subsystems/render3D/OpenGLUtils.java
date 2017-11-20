package asteroids.subsystems.render3D;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL15.*;

public class OpenGLUtils
{
	//return VBO from FloatBuffer
	static public int makeVBO(FloatBuffer inputData)
	{
		//create VBO
		int vertexBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
			glBufferData(GL_ARRAY_BUFFER, inputData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return vertexBuffer;
	}
	
	//return IBO from FloatBuffer
	static public int makeIBO(IntBuffer inputData)
	{
		//create IBO
		int indexBuffer = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, inputData, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		return indexBuffer;
	}
}
