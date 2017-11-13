package asteroids.components.Collider;

import asteroids.components.Collider.Shapes.*;
import asteroids.components.Component;
import asteroids.components.Geometry3D.Render3DMeshComponent;
import asteroids.components.Geometry3D.Transform3DComponent;
import asteroids.math.Transform3D;
import asteroids.math.Vector3f;

public class Collider3DComponent extends Component
{
	public Collider3DShape collider3DShape;
	public boolean isTrigger = false;
	public int target;
	public Vector3f position = new Vector3f();
	
	
	public Collider3DComponent()
	{
		this.collider3DShape = new Collider3DShapeBox();
	}
	
	//returns certain component in subclass casted form
	public <T extends Collider3DShape> T getShape(Class<T> c)
	{
		//int componentId = this.entityComponentRelations[entityId][getComponentId(c)];
		return c.cast(this.collider3DShape);
	}
	
	public void reset(Render3DMeshComponent render3DMeshComponent)
	{
		this.collider3DShape.reset(render3DMeshComponent);
		this.position = this.collider3DShape.getPosition();
	}

	public Vector3f getAABBSize (Transform3DComponent transform3DComponent)
	{
		return this.collider3DShape.getAABBSize(transform3DComponent);
	}
}
