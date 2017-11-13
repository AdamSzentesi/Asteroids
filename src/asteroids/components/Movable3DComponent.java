package asteroids.components;

import asteroids.math.Vector3f;

public class Movable3DComponent extends Component
{
	public Vector3f originalPosition = new Vector3f();
	public float speed = 1.0f;
	public int target;
}
