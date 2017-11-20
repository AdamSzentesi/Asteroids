package asteroids.components.Collider.Shapes;

import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import asteroids.subsystems.physics2D.Physics2DAABB;

public class Collider2DShapeCircle extends Collider2DShape
{
	public float radius;
	
	public Collider2DShapeCircle(float radius)
	{
		//this.debug2DPrimitive = new Debug2DPrimitiveCircle(1.0f);
		this.radius = radius;
		this.aabb = new Physics2DAABB(new Vector2f(-radius, -radius), new Vector2f(radius, radius));
		this.shapeKey = 1 << 1;
	}
	
	@Override
	public void updateAABB(Matrix4f rotationScaleMatrix)
	{
		this.aabb.min.set(-this.radius, -this.radius);
		this.aabb.max.set(this.radius, this.radius);
	}
	
}
