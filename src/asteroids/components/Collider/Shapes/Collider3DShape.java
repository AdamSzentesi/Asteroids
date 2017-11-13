package asteroids.components.Collider.Shapes;

import asteroids.components.Geometry3D.Render3DMeshComponent;
import asteroids.components.Geometry3D.Transform3DComponent;
import asteroids.math.Vector3f;

public class Collider3DShape
{
	public void reset(Render3DMeshComponent render3DMeshComponent){}
	public Vector3f getPosition(){return new Vector3f();}
	public Vector3f getAABBSize (Transform3DComponent transform3DComponent){return new Vector3f();}
}
