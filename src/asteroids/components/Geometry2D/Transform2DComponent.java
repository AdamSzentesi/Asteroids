package asteroids.components.Geometry2D;

import asteroids.components.Component;
import asteroids.math.Matrix4f;
import asteroids.math.Transform2D;
import asteroids.math.Vector2f;

public class Transform2DComponent extends Component
{
	public Transform2D transform;
	public Transform2D lastTransform;	
	
	public Vector2f lastCollisionNormal;
	public boolean apply = true;
	
	//hierarchy
	public boolean child = false;
	public Transform2DComponent parentComponent;
	
	public Transform2DComponent()
	{
		this.transform = new Transform2D();
		this.lastTransform = new Transform2D();
		this.lastCollisionNormal = new Vector2f();
	}
	
	public void rotate(float angle)
	{
		this.transform.rotation = this.transform.rotation + angle;
	}
	
	public void setParent(Transform2DComponent transform2DComponent)
	{
		this.child = true;
		this.parentComponent = transform2DComponent;
	}

	public Matrix4f getMatrix(boolean uniformScale)
	{
		return this.transform.getMatrix(uniformScale);
	}
	
	public Matrix4f getWorldMatrix()
	{
		return getWorldMatrix(false);
	}
	public Matrix4f getWorldMatrix(boolean uniformScale)
	{
		Matrix4f result = new Matrix4f().initIdentity();
		if(this.child)
		{
			result = this.parentComponent.getWorldMatrix(uniformScale);
		}
		return result.multiply(getMatrix(uniformScale));
	}

	
	
	public Matrix4f getMatrixPR()
	{
		return this.transform.getMatrixPR();
	}
	public Matrix4f getWorldMatrixPR()
	{
		Matrix4f result = new Matrix4f().initIdentity();
		if(this.child)
		{
			result = this.parentComponent.getWorldMatrixPR();
		}
		return result.multiply(getMatrixPR());
	}
	
	public float getWorldRotation()
	{
		float result = this.transform.rotation;
		if(this.child)
		{
			result = result + this.parentComponent.getWorldRotation();
		}
		return result;
	}
	
	public void setScale(float x)
	{
		this.transform.setScale(x, x);
	}

	public void setScale(float x, float y)
	{
		this.transform.setScale(x, y);
	}
}
