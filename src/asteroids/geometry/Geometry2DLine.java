package asteroids.geometry;

import asteroids.math.Vector2f;

public class Geometry2DLine
{
	public Vector2f a;
	public Vector2f b;
	public Vector2f n;
	
	public Geometry2DLine(Vector2f a, Vector2f b, Vector2f n)
	{
		this.a = a;
		this.b = b;
		this.n = n;
	}

}
