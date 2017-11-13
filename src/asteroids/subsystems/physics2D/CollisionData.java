package asteroids.subsystems.physics2D;

import asteroids.math.Vector2f;

public class CollisionData
{
	public boolean collided;
	public Vector2f collisionNormal;
	public float collisionTime;
	
	public CollisionData(boolean collided, Vector2f collisionNormal, float collisionTime)
	{
		this.collided = collided;
		this.collisionNormal = collisionNormal;
		this.collisionTime = collisionTime;
	}
	
	public CollisionData()
	{
		this(false, new Vector2f(), 0);
	}
}
