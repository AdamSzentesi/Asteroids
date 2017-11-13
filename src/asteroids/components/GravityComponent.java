package asteroids.components;

import asteroids.math.Vector3f;

public class GravityComponent extends Component
{
	public Vector3f magnitude = new Vector3f(0.0f, -10.0f, 0.0f);
}
