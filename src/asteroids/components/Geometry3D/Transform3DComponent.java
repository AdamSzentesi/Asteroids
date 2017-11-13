package asteroids.components.Geometry3D;

import asteroids.components.Component;
import asteroids.math.Matrix4f;
import asteroids.math.Quaternion;
import asteroids.math.Transform3D;
import asteroids.math.Vector3f;

public class Transform3DComponent extends Component
{
	public Transform3D transform;
	public Transform3D lastTransform;
	public Transform3D worldTransform;
	public Matrix4f worldMatrix = new Matrix4f().initIdentity();
	public Matrix4f viewMatrix = new Matrix4f().initIdentity();
	
	public boolean apply = false;
	
	public boolean child = false;
	public int parent;
	

	public Transform3DComponent()
	{
		this.transform = new Transform3D();
		this.lastTransform = new Transform3D();
		this.worldTransform = new Transform3D();
	}
	
	public Matrix4f getMatrix(Transform3D transform)
	{
		Matrix4f position = new Matrix4f().initTranslation(transform.position);
		Matrix4f rotation = transform.rotation.toRotationMatrix();
		Matrix4f scale = new Matrix4f().initScale(transform.scale);
		
		Matrix4f result = position.multiply(rotation.multiply(scale));
		return result;
	}
		
	//get camera space transformation matrix
	public Matrix4f viewMatrix()
	{
		return this.viewMatrix;
	}
	
	public void rotate(Vector3f axis, float angle)
	{
		Quaternion rotationQ = new Quaternion(axis, (float)Math.toRadians(angle));
		this.transform.rotation = rotationQ.multiply(this.transform.rotation).normalize();
	}
	
	public void setParent(int entityId)
	{
		this.child = true;
		this.parent = entityId;
	}
}