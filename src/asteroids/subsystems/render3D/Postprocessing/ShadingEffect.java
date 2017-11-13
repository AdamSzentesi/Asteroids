package asteroids.subsystems.render3D.Postprocessing;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import asteroids.components.Geometry3D.PointLightComponent;
import asteroids.components.Geometry3D.Transform3DComponent;
import asteroids.math.Vector3f;
import asteroids.subsystems.render3D.Shader;

public class ShadingEffect extends Effect
{
	public ShadingEffect(int width, int height)
	{
		super(width, height);

		Shader shader = new Shader();
		shader.addShader("Render3D/shading.vs", GL_VERTEX_SHADER);
		shader.addShader("Render3D/shading.fs", GL_FRAGMENT_SHADER);
		shader.addAttribute(0, "position");
		shader.addAttribute(1, "tex");
		shader.link();
		shader.addUniform("viewPosition");
		shader.addUniform("diffuseSampler");
		shader.addUniform("normalSampler");
		shader.addUniform("specularSampler");
		shader.addUniform("emissionSampler");
		shader.addUniform("positionSampler");
		shader.addUniform("numberOfLights");
		
		super.setShader(shader);
	}
	
	public int apply(PointLightComponent[] pointLights, Transform3DComponent[] transformLights, Vector3f viewPosition, int diffuseTextureId, int normalTextureId, int specularTextureId, int emissionTextureId, int positionTextureId)
	{
		bindFramebuffer();
		glDisable(GL_DEPTH_TEST);

		glUseProgram(shader.getId());
		shader.setUniform("viewPosition", viewPosition);
		shader.setUniform("diffuseSampler", (int)0);
		shader.setUniform("normalSampler", (int)1);
		shader.setUniform("specularSampler", (int)2);
		shader.setUniform("emissionSampler", (int)3);
		shader.setUniform("positionSampler", (int)4);
		//update lights
		for(int i = 0; i < pointLights.length; i++)
		{
			PointLightComponent pointLight = pointLights[i];
			Transform3DComponent transformLight = transformLights[i];
			shader.setUniform("pointLights", pointLight, transformLight, i);
		}
		shader.setUniform("numberOfLights", pointLights.length);
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, diffuseTextureId);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, normalTextureId);
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, specularTextureId);
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, emissionTextureId);
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, positionTextureId);

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
