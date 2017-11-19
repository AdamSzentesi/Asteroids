package asteroids.components.Collider;

import asteroids.components.Collider.Shapes.*;
import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import asteroids.subsystems.physics2D.Physics2DAABB;

public class Collider2DCircleComponent extends Collider2DComponent
{
	public Collider2DShape collider2DShape;
	public Vector2f position = new Vector2f();
	public Physics2DAABB aabb = new Physics2DAABB(new Vector2f(-0.1f, -0.1f), new Vector2f(0.1f, 0.1f));
	public boolean isTrigger = false;
	public int target;
	
	public Collider2DCircleComponent()
	{
		this.collider2DShape = new Collider2DShapeCircle();
	}
	
	//returns certain component in subclass casted form
	public <T extends Collider2DShape> T getShape(Class<T> c)
	{
		//int componentId = this.entityComponentRelations[entityId][getComponentId(c)];
		return c.cast(this.collider2DShape);
	}
	
	public Vector2f[] getAABBSize (Matrix4f rotationMatrix)
	{
		return this.collider2DShape.getAABBSize(rotationMatrix);
	}
	
	public Vector2f getColliderSize()
	{
		return this.collider2DShape.getColliderSize();
	}
	
	public int getShapeKey()
	{
		return this.collider2DShape.shapeKey;
	}
}