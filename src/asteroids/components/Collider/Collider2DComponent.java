package asteroids.components.Collider;

import asteroids.components.Collider.Shapes.*;
import asteroids.components.Component;
import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import asteroids.subsystems.physics2D.Physics2DAABB;

public class Collider2DComponent extends Component
{
	private Collider2DShape collider2DShape;
	public Vector2f position = new Vector2f();
	public boolean isTrigger = false;
	public int target;
	
	public Collider2DComponent()
	{
		this.collider2DShape = new Collider2DShapeRectangle(1, 1);
	}
	
	//returns certain component in subclass casted form
	public <T extends Collider2DShape> T getShape(Class<T> c)
	{
		//int componentId = this.entityComponentRelations[entityId][getComponentId(c)];
		return c.cast(this.collider2DShape);
	}

	public int getShapeKey()
	{
		return this.collider2DShape.shapeKey;
	}
	
	public Physics2DAABB getAABB(Matrix4f rotationScaleMatrix)
	{
		return this.collider2DShape.getAABB(rotationScaleMatrix);
	}
	
	public void setShape(Collider2DShape collider2DShape)
	{
		this.collider2DShape = collider2DShape;
	}
	
}
