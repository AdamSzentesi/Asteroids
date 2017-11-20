package asteroids.components.Collider.Shapes;

import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import asteroids.subsystems.physics2D.Physics2DAABB;

public class Collider2DShapePoint extends Collider2DShape
{
	public Collider2DShapePoint()
	{
		//this.debug2DPrimitive = new Debug2DPrimitiveLine(new Vector2f(), new Vector2f(0.0f, -0.05f));
		this.aabb = new Physics2DAABB(new Vector2f(), new Vector2f());
		this.shapeKey = 1 << 5;
	}
	
	@Override
	public void updateAABB(Matrix4f rotationScaleMatrix)
	{	
	}
	
}
