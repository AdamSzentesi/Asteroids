package asteroids.components.Geometry3D;

import asteroids.components.Component;
import asteroids.math.Vector3f;

public class Rigidbody3DComponent extends Component
{
	public float mass = 0;
	public Vector3f velocity = new Vector3f();
}
