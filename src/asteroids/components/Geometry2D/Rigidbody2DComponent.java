package asteroids.components.Geometry2D;

import asteroids.components.Component;
import asteroids.math.Vector2f;

public class Rigidbody2DComponent extends Component
{
	public float mass = 0;
	public boolean gravity = true;
	public Vector2f velocity = new Vector2f();
	public Vector2f lastVelocity = new Vector2f();
	public Vector2f acceleration = new Vector2f();
	public float maxVelocity = 0;
}
