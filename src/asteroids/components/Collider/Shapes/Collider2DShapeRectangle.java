package asteroids.components.Collider.Shapes;

import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import asteroids.subsystems.physics2D.Physics2DAABB;

public class Collider2DShapeRectangle extends Collider2DShape
{
	private Vector2f[] vertices;
	
	public Collider2DShapeRectangle(float x, float y)
	{
		//this.debug2DPrimitive = new Debug2DPrimitiveRectangle();
		this.vertices = new Vector2f[]
		{
			new Vector2f(-x/2, y/2),
			new Vector2f(x/2, y/2),
			new Vector2f(x/2, -y/2),
			new Vector2f(-x/2, -y/2)
		};
		this.aabb = new Physics2DAABB(new Vector2f(-x/2, -x/2), new Vector2f(x/2, x/2));
		this.shapeKey = 1 << 4;
	}
	
	@Override
	public void updateAABB(Matrix4f rotationScaleMatrix)
	{
		float minX = 0;
		float minY = 0;
		float maxX = 0;
		float maxY = 0;
		
		for(Vector2f vertex : this.vertices)
		{
			Vector2f translatedVertex = rotationScaleMatrix.transform(vertex);
			this.aabb.min.x = Math.min(minX, translatedVertex.x);
			this.aabb.min.y = Math.min(minY, translatedVertex.y);
			this.aabb.max.x = Math.max(maxX, translatedVertex.x);
			this.aabb.max.y = Math.max(maxY, translatedVertex.y);
		}
	}
	
}
