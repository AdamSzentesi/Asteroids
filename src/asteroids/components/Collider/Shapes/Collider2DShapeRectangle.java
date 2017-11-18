package asteroids.components.Collider.Shapes;

import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import asteroids.subsystems.render2D.primitives.Debug2DPrimitiveRectangle;

public class Collider2DShapeRectangle extends Collider2DShape
{
	private Vector2f size;
	private Vector2f[] vertices = new Vector2f[4];
	
	public Collider2DShapeRectangle()
	{
		this(1.0f, 1.0f);
	}
	
	public Collider2DShapeRectangle(float x, float y)
	{
		this.size = new Vector2f(x, y);
		//this.debug2DPrimitive = new Debug2DPrimitiveRectangle();
		this.vertices = new Vector2f[]
		{
			new Vector2f(-x/2, y/2),
			new Vector2f(x/2, y/2),
			new Vector2f(x/2, -y/2),
			new Vector2f(-x/2, -y/2)
		};
		this.shapeKey = 1 << 4;
	}
	
	@Override
	public Vector2f[] getAABBSize (Matrix4f rotationScaleMatrix)
	{
		float minX = 0;
		float minY = 0;
		float maxX = 0;
		float maxY = 0;
		
		for(Vector2f vertex : this.vertices)
		{
			Vector2f translatedVertex = rotationScaleMatrix.transform(vertex);
			minX = Math.min(minX, translatedVertex.x);
			minY = Math.min(minY, translatedVertex.y);
			maxX = Math.max(maxX, translatedVertex.x);
			maxY = Math.max(maxY, translatedVertex.y);
		}
		
		return new Vector2f[]
		{
			new Vector2f(minX, minY),
			new Vector2f(maxX, maxY)
		};
		//return new Vector2f(maxX*2, maxY*2);
	}
	
	@Override
	public Vector2f getColliderSize()
	{
		return this.size;
	}
}
