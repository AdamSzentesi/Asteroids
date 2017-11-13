package asteroids.subsystems.render3D;

import asteroids.components.CameraComponent;
import asteroids.components.Geometry3D.Render3DMesh.Material;
import asteroids.math.Matrix4f;
import asteroids.math.Vector3f;

public class TronShader extends Shader
{
	@Override
	public void updateUniforms(Matrix4f modelMatrix, Matrix4f viewMatrix, CameraComponent cameraCamera, Material material, Vector3f viewPosition)
	{
		Matrix4f projectionMatrix = cameraCamera.projection;
		
		Matrix4f M = modelMatrix;
		Matrix4f V = viewMatrix;
		Matrix4f P = projectionMatrix;
		
		setUniform("M", M);
		setUniform("V", V);
		setUniform("P", P);
		
		setUniform("specularIntensity", material.specularIntensity);
		setUniform("specularExponent", material.specularExponent);
		
		setUniform("diffuseSampler", (int)0);
		setUniform("specularSampler", (int)1);
		setUniform("normalSampler", (int)2);
		setUniform("emissionSampler", (int)3);
	}	
}
