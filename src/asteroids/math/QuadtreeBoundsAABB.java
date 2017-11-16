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
	
	//very slow!!!
	public boolean isInside(Physics2DAABB box)
	{
		boolean result = false;
		float halfWidth = box.getWidth() / 2;
		float halfHeight = box.getHeight() / 2;
		float positionX = box.min.x + halfWidth;
		float positionY = box.min.y + halfHeight;

		if
		(
			positionX >= this.min.x - halfWidth &&
			positionX <= this.max.x + halfWidth &&
			positionY >= this.min.y - halfHeight &&
			positionY <= this.max.y + halfHeight
		)
		{
			result = true;
		}
//		System.out.println(isInside(box.min) + " | " + isInside(box.max));

		return result;
	}
	
}
