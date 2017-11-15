package asteroids.math;

public class QuadtreeBounds
{
	public Vector2f min;
	public Vector2f max;
	
	public QuadtreeBounds(Vector2f min, Vector2f max)
	{
		this.min = min;
		this.max = max;
	}
	
	public QuadtreeBounds(Vector2f min, float width, float height)
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
	
	public boolean inInside(Vector2f point)
	{
		boolean result = true;
		if(point.x < this.min.x || point.x > this.max.x || point.y < this.min.y || point.y > this.max.y)
		{
			result = false;
		}
		return result;
	}
}
