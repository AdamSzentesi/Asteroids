package asteroids.components.Collider.Shapes;

import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import asteroids.subsystems.physics2D.Physics2DAABB;

public class Collider2DShapeLine extends Collider2DShape
{
	public Vector2f startPoint;
	public Vector2f endPoint = new Vector2f();
	
	public Collider2DShapeLine(Vector2f startPoint, Vector2f endPoint)
	{
		//this.debug2DPrimitive = new Debug2DPrimitiveLine(startPoint, endPoint);
		this.startPoint = new Vector2f(startPoint.x, startPoint.y);
		this.endPoint = new Vector2f(endPoint.x, endPoint.y);
		this.aabb = new Physics2DAABB(new Vector2f(), new Vector2f());
		update(startPoint, endPoint);
		this.shapeKey = 1 << 2;
	}
	
	@Override
	public void updateAABB(Matrix4f rotationScaleMatrix)
	{
		Vector2f transformedStart = rotationScaleMatrix.transform(this.startPoint);
		Vector2f transformedEnd = rotationScaleMatrix.transform(this.endPoint);
		
		update(transformedStart, transformedEnd);
	}

	private void update(Vector2f transformedStart, Vector2f transformedEnd)
	{
		float minX = Math.min(transformedStart.x, transformedEnd.x);
		float maxX = Math.max(transformedStart.x, transformedEnd.x);
		float minY = Math.min(transformedStart.y, transformedEnd.y);
		float maxY = Math.max(transformedStart.y, transformedEnd.y);
		
		this.aabb.min.set(minX, minY);
		this.aabb.max.set(maxX, maxY);	
	}
	
}
