package asteroids.subsystems.render3D.Postprocessing;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import asteroids.subsystems.render3D.Shader;

public class DrawEffect extends Effect
{
	public DrawEffect(int width, int height)
	{
		super(width, height);

		Shader shader = new Shader();
		shader.addShader("effects/draw.vs", GL_VERTEX_SHADER);
		shader.addShader("effects/draw.fs", GL_FRAGMENT_SHADER);
		shader.addAttribute(0, "position");
		shader.addAttribute(1, "tex");
		shader.link();
		shader.addUniform("diffuseSampler");
		super.setShader(shader);
	}
		
	@Override
	public void bindFramebuffer()
	{
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
}
