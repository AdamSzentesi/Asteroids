package asteroids.components.Collider.Shapes;

import asteroids.components.Geometry3D.Render3DMeshComponent;
import asteroids.components.Geometry3D.Transform3DComponent;
import asteroids.math.Vector3f;

public class Collider3DShapeBox extends Collider3DShape
{
	public Vector3f size = new Vector3f(1.0f, 1.0f, 1.0f);
	public Vector3f position = new Vector3f();
	public Vector3f[] defaultAABBVertices = new Vector3f[8];
					
	@Override
	public void reset(Render3DMeshComponent render3DMeshComponent)
	{
		//set the collider size based od mesh
		this.size.x = (render3DMeshComponent.max.x - render3DMeshComponent.min.x);
		this.size.y = (render3DMeshComponent.max.y - render3DMeshComponent.min.y);
		this.size.z = (render3DMeshComponent.max.z - render3DMeshComponent.min.z);
		//set the default AABB corner vertices
		this.defaultAABBVertices = new Vector3f[]
		{
			new Vector3f(this.size.x, this.size.y, this.size.z),
			new Vector3f(this.size.x, this.size.y, -this.size.z),
			new Vector3f(this.size.x, -this.size.y, this.size.z),
			new Vector3f(this.size.x, -this.size.y, -this.size.z),
			new Vector3f(-this.size.x, this.size.y, this.size.z),
			new Vector3f(-this.size.x, this.size.y, -this.size.z),
			new Vector3f(-this.size.x, -this.size.y, this.size.z),
			new Vector3f(-this.size.x, -this.size.y, -this.size.z),
		};
		//set the position to center 
		this.position = this.size.divide(2f).add(render3DMeshComponent.min);
	}
	
	@Override
	public Vector3f getPosition()
	{
		return this.position;
	}
	
	@Override
	public Vector3f getAABBSize (Transform3DComponent transform3DComponent)
	{
		Vector3f finalAABB = new Vector3f();
		//rotate default AABB vertices by model matrix
		for(Vector3f vertex : this.defaultAABBVertices)
		{
			Vector3f v = vertex.rotate(transform3DComponent.worldTransform.rotation);
			if(v.x > finalAABB.x){finalAABB.x = v.x;}
			if(v.y > finalAABB.y){finalAABB.y = v.y;}
			if(v.z > finalAABB.z){finalAABB.z = v.z;}
		}
		return finalAABB;
	}
}
