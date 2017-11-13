package asteroids.subsystems.render2D;

import asteroids.components.CameraComponent;
import asteroids.math.Matrix3f;
import asteroids.math.Matrix4f;
import asteroids.subsystems.render3D.Shader;

public class LineShader2D extends Shader
{
	@Override
	public void updateUniforms(Matrix3f modelMatrix, Matrix4f viewMatrix, CameraComponent cameraCamera)
	{
		Matrix4f projectionMatrix = cameraCamera.projection;
		
		Matrix3f MV = modelMatrix;
		Matrix4f P = projectionMatrix;
		
		setUniform("MV", MV);
		setUniform("P", P);
	}
}
