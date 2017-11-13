package asteroids.components.Collider.Shapes;

import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import asteroids.subsystems.render2D.primitives.Debug2DPrimitiveLine;

public class Collider2DShapeLine extends Collider2DShape
{
	public Vector2f startPoint;
	public Vector2f endPoint;
	
	public Collider2DShapeLine(Vector2f startPoint, Vector2f endPoint)
	{
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		
		this.debug2DPrimitive = new Debug2DPrimitiveLine(startPoint, endPoint);
		this.shapeKey = 1 << 2;
	}
	
	@Override
	public Vector2f[] getAABBSize (Matrix4f rotationScaleMatrix)
	{
		Vector2f transformedStart = rotationScaleMatrix.transform(this.startPoint);
		Vector2f transformedEnd = rotationScaleMatrix.transform(this.endPoint);
		
		float minX = Math.min(transformedStart.x, transformedEnd.x);
		float maxX = Math.max(transformedStart.x, transformedEnd.x);
		float minY = Math.min(transformedStart.y, transformedEnd.y);
		float maxY = Math.max(transformedStart.y, transformedEnd.y);
		return new Vector2f[]
		{
			new Vector2f(minX, minY),
			new Vector2f(maxX, maxY)
		};
	}
	
	@Override
	public Vector2f getColliderSize()
	{
		return new Vector2f(1, 1);
	}
}
