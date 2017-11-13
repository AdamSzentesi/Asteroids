package asteroids.subsystems.render3D.Postprocessing;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import asteroids.subsystems.render3D.Shader;

public class HorizontalBlurEffect extends Effect
{
	public HorizontalBlurEffect(int width, int height)
	{
		super(width, height);

		Shader shader = new Shader();
		shader.addShader("effects/hblur.vs", GL_VERTEX_SHADER);
		shader.addShader("effects/hblur.fs", GL_FRAGMENT_SHADER);
		shader.addAttribute(0, "position");
		shader.addAttribute(1, "tex");
		shader.link();
		shader.addUniform("diffuseSampler");
		shader.addUniform("targetWidth");
		super.setShader(shader);
	}
	
	@Override
	public void updateUniforms()
	{
		shader.setUniform("diffuseSampler", (int)0);
		shader.setUniform("targetWidth", (float)this.framebuffer.getWidth());
	}
}
