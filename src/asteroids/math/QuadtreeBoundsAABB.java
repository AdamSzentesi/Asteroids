package asteroids.math;

import asteroids.subsystems.physics2D.Physics2DAABB;

public class QuadtreeBoundsAABB
{
	public Vector2f min;
	public Vector2f max;
	
	public QuadtreeBoundsAABB(Vector2f min, Vector2f max)
	{
		this.min = min;
		this.max = max;
	}
	
	public QuadtreeBoundsAABB(Vector2f min, float width, float height)
	{
		this(min, min.add(new Vector2f(width, height)));
	}
	
	public float getWidth()
	{
		return (this.max.x - this.min.x);
	}
	
	public float getHeight()
	{
		return (this.max.y - this.min.y);
	}
	
	public boolean isInside(Physics2DAABB box)
	{
		boolean result = false;
		if(isInside(box.min) || isInside(box.min))
		{
			result = true;
		}
		return result;
	}
	
	public boolean isInside(Vector2f point)
	{
		boolean result = true;
		if(point.x < this.min.x || point.x > this.max.x || point.y < this.min.y || point.y > this.max.y)
		{
			result = false;
		}
		return result;
	}
}
