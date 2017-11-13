package asteroids.math;

public class Vector3f
{
	public float x;
	public float y;
	public float z;
	
	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f()
	{
		this(0.0f, 0.0f, 0.0f);
	}
	
	public float max()
	{
		return (float)Math.max(Math.max(x, y), z);
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public void setZ(float z)
	{
		this.z = z;
	}
		
	public float getX()
	{
		return this.x;
	}
			
	public float getY()
	{
		return this.y;
	}
	
	public float getZ()
	{
		return this.z;
	}
	
	public float length()
	{
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	public float dot(Vector3f r)
	{
		return x * r.getX() + y * r.getY() + z * r.getZ();
	}
	
	public Vector3f lerp(Vector3f destination, float factor)
	{
		return destination.subtract(this).multiply(factor).add(this);
	}
	
	public Vector3f cross(Vector3f r)
	{
		float x_ = y * r.getZ()  - z * r.getY();
		float y_ = z * r.getX()  - x * r.getZ();
		float z_ = x * r.getY()  - y * r.getX();
		
		return new Vector3f(x_, y_, z_);
	}
	
	public Vector3f normalize()
	{
		float length = this.length();
		if(length == 0)
		{
			return new Vector3f(0, 0, 0);
		}
		else
		{
			return new Vector3f(this.x / length, this.y / length, this.z / length);
		}
	}

	//rotate vector by an angle on an axis
	public Vector3f rotate(Vector3f axis, float angle)
	{
		float sinAngle = (float)Math.sin(-angle);
		float cosAngle = (float)Math.cos(-angle);

		return this.cross(axis.multiply(sinAngle)).add(           //Rotation on local X
				(this.multiply(cosAngle)).add(                     //Rotation on local Z
						axis.multiply(this.dot(axis.multiply(1 - cosAngle))))); //Rotation on local Y
	}

	//rotate vector by an angle on an axis
	public Vector3f rotate(Quaternion rotation)
	{
		Quaternion conjugate = rotation.conjugate();
		Quaternion w = rotation.multiply(this).multiply(conjugate);
		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}
	
	public Vector3f add(Vector3f r)
	{
		return new Vector3f(this.x + r.getX(), this.y + r.getY(), this.z + r.getZ());
	}
	
	public Vector3f add(float r)
	{
		return new Vector3f(x + r, y + r, z + r);
	}
	
	public Vector3f subtract(Vector3f r)
	{
		return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ());
	}
	
	public Vector3f subtract(float r)
	{
		return new Vector3f(x - r, y - r, z - r);
	}
	
	public Vector3f multiply(Vector3f r)
	{
		return new Vector3f(x * r.getX(), y * r.getY(), z * r.getZ());
	}
	
	public Vector3f multiply(float r)
	{
		return new Vector3f(x * r, y * r, z * r);
	}
	
	public Vector3f divide(Vector3f r)
	{
		return new Vector3f(x / r.getX(), y / r.getY(), z / r.getZ());
	}
	
	public Vector3f divide(float r)
	{
		return new Vector3f(x / r, y / r, z / r);
	}
	
	public boolean isEqual(Vector3f r)
	{
		System.out.println("Vector3f.equals: " + r);
		return (this.x == r.getX() && this.y == r.getY() && this.z == r.getZ());
	}
	
	public Vector3f set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	public Vector3f set(Vector3f r)
	{
		set(r.getX(), r.getY(), r.getZ());
		return this;
	}
	
	@Override
	public boolean equals(Object object)
	{
		Vector3f vector3f = (Vector3f)object;
		return x == vector3f.x && y == vector3f.y && z == vector3f.z;
	}
	
	@Override
	public int hashCode()
	{
		final int BASE = 17;
		final int MULTIPLIER = 31;
		int result = BASE;
		
		result = MULTIPLIER * result + (int)x;
		result = MULTIPLIER * result + (int)y;
		result = MULTIPLIER * result + (int)z;
	
		return result;
	}
}
