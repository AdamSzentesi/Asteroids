package asteroids.components.Collider.Shapes;

import asteroids.math.Matrix4f;
import asteroids.subsystems.physics2D.Physics2DAABB;

abstract public class Collider2DShape
{
	//public Debug2DPrimitive debug2DPrimitive;
	public int shapeKey = 1 << 0;
	public Physics2DAABB aabb;
	
	public Physics2DAABB getAABB(Matrix4f rotationScaleMatrix)
	{
		updateAABB(rotationScaleMatrix);
		return this.aabb;
	}
	
	abstract public void updateAABB(Matrix4f rotationScaleMatrix);
}
