package asteroids.subsystems.render3D.Postprocessing;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import asteroids.Util;
import asteroids.subsystems.render3D.Framebuffer;
import asteroids.subsystems.render3D.Shader;

public class Effect
{
	public Framebuffer framebuffer;
	public Shader shader;
	public int screenVBO;

	public Effect(int width, int height)
	{
		this.framebuffer = new Framebuffer(width, height);
		this.framebuffer.createTexturebuffer(GL_COLOR_ATTACHMENT0, GL_RGB, GL_UNSIGNED_BYTE, true);
		this.framebuffer.updateDrawbuffers();
		
		//setup screen geometry
		float[] screenVertexArray = new float[]
		{
			-1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 0.0f,
			 1.0f, -1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f
		};
		FloatBuffer vertexBuffer = Util.makeFlippedBuffer(screenVertexArray);		
		this.screenVBO = makeVBO(vertexBuffer);
	}
	
	public void setShader(Shader shader)
	{
		this.shader = shader;
	}
	
	public int apply(int textureId)
	{
		bindFramebuffer();
		glDisable(GL_DEPTH_TEST);

		glUseProgram(shader.getId());
		updateUniforms();
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureId);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);		

		glBindBuffer(GL_ARRAY_BUFFER, this.screenVBO);
			glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 4, 0);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * 4, 8);
			glDrawArrays(GL_TRIANGLES, 0, 6);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		return this.framebuffer.getTexture(0);
	}
	
	public void bindFramebuffer()
	{
		this.framebuffer.bind();
	}
	
	public void updateUniforms(){}
	
	//return VBO from FloatBuffer
	private int makeVBO(FloatBuffer inputData)
	{
		int vertexBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
			glBufferData(GL_ARRAY_BUFFER, inputData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		return vertexBuffer;
	}
	
	public void cleanUp()
	{
		this.framebuffer.cleanUp();
		glDeleteBuffers(this.screenVBO);
	}
	
	public int getOutput()
	{
		return this.framebuffer.getTexture(0);
	}
	
	public void enableBlending()
	{
		this.framebuffer.enableBlending();
	}
}
