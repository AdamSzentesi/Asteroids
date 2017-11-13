package asteroids.math;

public class Transform3D
{
	public Vector3f position;
	public Quaternion rotation;
	public Vector3f scale;
	
	public Transform3D()
	{
		this.position = new Vector3f(0,0,0);
		this.rotation = new Quaternion(0,0,0,1);
		this.scale = new Vector3f(1,1,1);
	}
	
	public Transform3D(Vector3f position, Quaternion rotation, Vector3f scale)
	{
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Transform3D transform(Matrix4f matrix, Transform3D transform)
	{
		Transform3D result = new Transform3D();
		result.position = matrix.transform(this.position);
		result.rotation = transform.rotation.multiply(this.rotation);
		result.scale = matrix.transform(this.scale);
	
		return result;
	}
}
