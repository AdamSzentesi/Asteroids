package asteroids.components;

import asteroids.math.Matrix4f;
import asteroids.math.Vector3f;

public class CameraComponent extends Component
{
	public static final Vector3f yAxis = new Vector3f(0, 1, 0);
	public Matrix4f projection;
	public Matrix4f viewMatrix;
	
	public CameraComponent()
	{
		this.projection = new Matrix4f().initPerspective(45f, 1/1f, 0.001f, 10f);
		this.viewMatrix = new Matrix4f().initIdentity();
	}
}
