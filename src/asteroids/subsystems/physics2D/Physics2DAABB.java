package asteroids.subsystems.physics2D;

import asteroids.math.Vector2f;

public class Physics2DAABB
{
	public Vector2f min;
	public Vector2f max;
	
	public Physics2DAABB(Vector2f min, Vector2f max)
	{
		this.min = min;
		this.max = max;
	}
	
	public float getWidth()
	{
		return (this.max.x - this.min.x);
	}
	
	public float getHeight()
	{
		return (this.max.y - this.min.y);
	}
}
