package asteroids.subsystems.render3D;

import asteroids.components.CameraComponent;
import asteroids.math.Matrix4f;

public class SectionShader extends Shader
{
	@Override
	public void updateUniforms(Matrix4f modelMatrix, Matrix4f viewMatrix, CameraComponent cameraCamera)
	{
		Matrix4f projectionMatrix = cameraCamera.projection;
		
		Matrix4f M = modelMatrix;
		Matrix4f P = projectionMatrix;
		
		setUniform("M", M);
		setUniform("P", P);
	}
}
