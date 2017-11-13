package asteroids.subsystems.render3D.Postprocessing;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import asteroids.subsystems.render3D.Shader;

public class CombineEffect extends Effect
{
	private int samplerAId;
	private int samplerBId;
	
	public CombineEffect(int width, int height)
	{
		super(width, height);

		Shader shader = new Shader();
		shader.addShader("effects/combine.vs", GL_VERTEX_SHADER);
		shader.addShader("effects/combine.fs", GL_FRAGMENT_SHADER);
		shader.addAttribute(0, "position");
		shader.addAttribute(1, "tex");
		shader.link();
		shader.addUniform("samplerA");
		shader.addUniform("strengthA");
		shader.addUniform("samplerB");
		shader.addUniform("strengthB");
		super.setShader(shader);
	}
	
	public void updateUniforms(float strengthA, float strengthB)
	{
		shader.setUniform("samplerA", (int)0);
		shader.setUniform("strengthA", strengthA);
		shader.setUniform("samplerB", (int)1);
		shader.setUniform("strengthB", strengthB);
	}
	
	public int apply(int textureAId, float strengthA, int textureBId, float strengthB)
	{
		bindFramebuffer();
		glDisable(GL_DEPTH_TEST);

		glUseProgram(shader.getId());
		updateUniforms(strengthA, strengthB);
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureAId);
		
		glActiveTexture(GL_TEXTURE0 + 1);
		glBindTexture(GL_TEXTURE_2D, textureBId);

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
}
