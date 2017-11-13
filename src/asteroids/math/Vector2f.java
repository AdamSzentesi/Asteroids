package asteroids.math;

public class Vector2f
{
	public float x;
	public float y;
	
	public Vector2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2f()
	{
		this(0.0f, 0.0f);
	}
	
	public float max()
	{
		return Math.max(x, y);
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
		
	public float getX()
	{
		return this.x;
	}
			
	public float getY()
	{
		return this.y;
	}
	
	public float length()
	{
		return (float)Math.sqrt(x * x + y * y);
	}
	
	public float dot(Vector2f r)
	{
		return (x * r.getX() + y * r.getY());
	}
	
	public float cross(Vector2f r)
	{
		return (x * r.getY() - y * r.getX());
	}
	
	public Vector2f lerp(Vector2f destination, float factor)
	{
		return destination.subtract(this).multiply(factor).add(this);
	}
	
	public Vector2f normalize()
	{
		float length = this.length();
		
		//this.x /= length;
		//this.y /= length;
		
		return new Vector2f(x / length, y / length);
	}
	
	public Vector2f rotate(float angle)
	{
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		
		return new Vector2f((float)(x * cos - y * sin),(float)(x * sin + y * cos));
	}
	
	public Vector2f add(Vector2f r)
	{
		return new Vector2f(x + r.getX(), y + r.getY());
	}
	
	public Vector2f add(float r)
	{
		return new Vector2f(x + r, y + r);
	}
	
	public Vector2f subtract(Vector2f r)
	{
		return new Vector2f(x - r.getX(), y - r.getY());
	}
	
	public Vector2f subtract(float r)
	{
		return new Vector2f(x - r, y - r);
	}
	
	public Vector2f multiply(Vector2f r)
	{
		return new Vector2f(x * r.getX(), y * r.getY());
	}
	
	public Vector2f multiply(float r)
	{
		return new Vector2f(x * r, y * r);
	}
	
	public Vector2f divide(Vector2f r)
	{
		return new Vector2f(x / r.getX(), y / r.getY());
	}
	
	public Vector2f divide(float r)
	{
		return new Vector2f(x / r, y / r);
	}
	
	public boolean isEqual(Vector2f r)
	{
		return (this.x == r.getX() && this.y == r.getY());
	}
	
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void set(Vector2f vector)
	{
		set(vector.getX(), vector.getY());
	}

	public Vector2f turnCW()
	{
		return new Vector2f(this.y, this.x);
	}
	
	public Vector2f turnCCW()
	{
		return new Vector2f(-this.y, this.x);
	}
	
	public float getAngle()
	{
		float result = (float)Math.toDegrees(Math.atan2(y, x));
		return result;
	}

	public float lengthSquared()
	{
		return (x * x + y * y);
	}

}
