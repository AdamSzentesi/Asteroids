package asteroids.components.Collider.Shapes;

import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import asteroids.subsystems.render2D.primitives.Debug2DPrimitiveCircle;

public class Collider2DShapeCircle extends Collider2DShape
{
	public float radius;
	
	public Collider2DShapeCircle()
	{
		this(1.0f);
	}
	
	public Collider2DShapeCircle(float radius)
	{
		this.radius = radius;
		this.debug2DPrimitive = new Debug2DPrimitiveCircle(1.0f);
		this.shapeKey = 1 << 1;
	}
	
	@Override
	public Vector2f[] getAABBSize (Matrix4f rotationMatrix)
	{
		return new Vector2f[]
		{
			new Vector2f(-this.radius, -this.radius),
			new Vector2f(this.radius, this.radius)
		};
	}
	
	@Override
	public Vector2f getAABBMax (Matrix4f rotationMatrix)
	{
		return new Vector2f(this.radius, this.radius);
	}
	
	@Override
	public Vector2f getColliderSize()
	{
		return new Vector2f(this.radius, this.radius);
	}
}
