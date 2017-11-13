package asteroids.components.Collider.Shapes;

import asteroids.components.Geometry3D.Render3DMeshComponent;
import asteroids.components.Geometry3D.Transform3DComponent;
import asteroids.math.Vector3f;

public class Collider3DShapeSphere extends Collider3DShape
{
	public float radius = 1.0f;
	
	@Override
	public void reset(Render3DMeshComponent render3DMeshComponent)
	{
		//TBD: algorithm to encapsulate all mesh points in a sphere diameter
	}

	@Override
	public Vector3f getAABBSize (Transform3DComponent transform3DComponent)
	{
		Vector3f result = new Vector3f(this.radius, this.radius, this.radius);
		return result.multiply(2f);
	}
}
