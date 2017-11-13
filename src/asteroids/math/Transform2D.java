package asteroids.math;

public class Transform2D
{
	public Vector2f position;
	public float rotation;
	public Vector2f scale;
	
	public Transform2D()
	{
		this.position = new Vector2f(0,0);
		this.rotation = 0f;
		this.scale = new Vector2f(1,1);
	}
	
	public Transform2D(Vector2f position, float rotation, Vector2f scale)
	{
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public Matrix4f getMatrix()
	{
		return getMatrix(false);
	}
	public Matrix4f getMatrix(boolean uniformScale)
	{
		Matrix4f position = new Matrix4f().initTranslation(this.position.x, this.position.y, 0);
		Matrix4f rotation = new Matrix4f().initRotation(0, 0, this.rotation);
		Matrix4f scale;
		if(uniformScale)
		{
			scale = new Matrix4f().initScale(this.scale.x, this.scale.x, 1);
		}
		else
		{
			scale = new Matrix4f().initScale(this.scale.x, this.scale.y, 1);
		}
		
		return position.multiply(rotation.multiply(scale));
	}
	
	public Matrix4f getMatrixPR()
	{
		Matrix4f position = new Matrix4f().initTranslation(this.position.x, this.position.y, 0);
		Matrix4f rotation = new Matrix4f().initRotation(0, 0, this.rotation);
		
		return position.multiply(rotation);
	}
	
	public void setScale(float x, float y)
	{
		this.scale.set(x, y);
	}

	public void set(Transform2D transform2D)
	{
		this.position.set(transform2D.position);
		this.rotation = transform2D.rotation;
		this.scale.set(transform2D.scale);
	}
}
