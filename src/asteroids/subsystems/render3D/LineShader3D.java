package asteroids.subsystems.render3D;

import asteroids.components.CameraComponent;
import asteroids.math.Matrix4f;

public class LineShader3D extends Shader
{
	@Override
	public void updateUniforms(Matrix4f modelMatrix, Matrix4f viewMatrix, CameraComponent cameraCamera)
	{
		Matrix4f MV = viewMatrix.multiply(modelMatrix);
		Matrix4f P = cameraCamera.projection;
		
		setUniform("MV", MV);
		setUniform("P", P);

	}
}
