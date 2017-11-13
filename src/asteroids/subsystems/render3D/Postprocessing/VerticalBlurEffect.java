package asteroids.subsystems.render3D.Postprocessing;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import asteroids.subsystems.render3D.Shader;

public class VerticalBlurEffect extends Effect
{
	public VerticalBlurEffect(int width, int height)
	{
		super(width, height);

		Shader shader = new Shader();
		shader.addShader("effects/vblur.vs", GL_VERTEX_SHADER);
		shader.addShader("effects/vblur.fs", GL_FRAGMENT_SHADER);
		shader.addAttribute(0, "position");
		shader.addAttribute(1, "tex");
		shader.link();
		shader.addUniform("diffuseSampler");
		shader.addUniform("targetHeight");
		super.setShader(shader);
	}
	
	@Override
	public void updateUniforms()
	{
		shader.setUniform("diffuseSampler", (int)0);
		shader.setUniform("targetHeight", (float)this.framebuffer.getHeight());
	}
}
