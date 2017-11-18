package asteroids.components.Geometry2D;

import asteroids.components.Component;
import asteroids.math.Matrix4f;
import asteroids.math.Transform2D;
import asteroids.math.Vector2f;
import asteroids.math.Vector3f;

public class Transform2DComponent extends Component
{
	public Transform2D transform;
	public Transform2D lastTransform;	
	
	public Vector2f lastCollisionNormal;
	public boolean apply = true;
	
	//hierarchy
	public boolean child = false;
	private Transform2DComponent parentComponent;
	public boolean inheritRotation = true;
	
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
	
	public Transform2DComponent getParent(Transform2DComponent transform2DComponent)
	{
		return this.parentComponent;
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
			result = this.parentComponent.getWorldMatrix(uniformScale, this.inheritRotation);
		}
		result = result.multiply(getMatrix(uniformScale));
		
		return result;
	}
	public Matrix4f getWorldMatrix(boolean uniformScale, boolean inheritRotation)
	{
		Matrix4f result = new Matrix4f().initIdentity();
		if(this.child)
		{
			result = this.parentComponent.getWorldMatrix(uniformScale, this.inheritRotation);
		}
		result = result.multiply(getMatrix(uniformScale));
		
		if(inheritRotation == false)
		{
			Vector2f position = result.transform(this.transform.position);
			result = new Matrix4f().initTranslation(new Vector3f(position.x, position.y, 0));
		}
		return result;
	}
	
	public Matrix4f getViewMatrix()
	{
		Matrix4f result = new Matrix4f().initIdentity();
		if(this.child)
		{
			result = this.parentComponent.getViewMatrix();
		}
		return this.transform.getReverseMatrix().multiply(result);
	}
	
//	private Matrix4f getWorldPositionMatrix()
//	{
//		Matrix4f result = new Matrix4f().initIdentity();
//		if(this.child)
//		{
//			result = this.parentComponent.getWorldMatrix(uniformScale);
//		}
//	}
	
//	public Vector2f getWorldPosition()
//	{
//		Vector2f result = new Vector2f();
//		if(this.child)
//		{
//			result = this.parentComponent.getWorldPosition();
//		}
//		return result.add(this.transform.position);
//	}
	
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
