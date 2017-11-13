package asteroids.subsystems;

import asteroids.Message;
import asteroids.World;
import asteroids.components.Collider.Collider2DComponent;
import asteroids.components.Collider.Shapes.*;
import asteroids.components.Geometry2D.Rigidbody2DComponent;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.geometry.GeometryUtils;
import asteroids.math.Vector2f;
import asteroids.subsystems.physics2D.*;
import java.util.List;

public class SimplePhysics2DSubsystem extends Subsystem
{
	private final float distanceBuffer = 0.01f;
	
	private final CollisionTestPointCircle collisionTestPointCircle = new CollisionTestPointCircle();
	private final CollisionTestCircleLine collisionTestCircleLine = new CollisionTestCircleLine();
	private final CollisionTestCircleCircle collisionTestCircleCircle = new CollisionTestCircleCircle();
	private final CollisionTestLineLine collisionTestLineLine = new CollisionTestLineLine();
	private final CollisionTestCircleMultiline collisionTestCircleMultiline = new CollisionTestCircleMultiline();
	private final CollisionTestLineMultiline collisionTestLineMultiline = new CollisionTestLineMultiline();
	
	private final int shapeKeyCircle = 1 << 1;
	private final int shapeKeyLine = 1 << 2;
	private final int shapeKeyMultiline = 1 << 3;
	private final int shapeKeyRectangle = 1 << 4;
	private final int shapeKeyPoint = 1 << 5;

	@Override
	public void iterate(World world, float delta)
	{
		Vector2f netAcceleration = new Vector2f();
		boolean enableCollision = true;
		
//SIMULATIONS
		for(int entityId : this.getList("primary"))
		{
			Rigidbody2DComponent rigidbody2DComponent = world.getComponent(entityId, Rigidbody2DComponent.class);
			rigidbody2DComponent.lastVelocity.set(rigidbody2DComponent.velocity);
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			transform2DComponent.lastTransform.set(transform2DComponent.transform);
			
			//ACCELERATIONS
			netAcceleration.set(0, 0);
				
			//FORCES
			if(rigidbody2DComponent.mass != 0)
			{
				//VELOCITIES	
				rigidbody2DComponent.velocity = rigidbody2DComponent.velocity.add(netAcceleration.multiply(delta));
				
				//MESSAGES
				List<Message> messages = getMessages(entityId);
				for(Message message : messages)
				{
					switch (message.parameter)
					{
						case "APPLY_FORCE":
						{
							Vector2f addAcceleration = message.getValue(Vector2f.class).divide(rigidbody2DComponent.mass);
							Vector2f addVelocity = addAcceleration.multiply(delta);
							rigidbody2DComponent.velocity = rigidbody2DComponent.velocity.add(addVelocity);
							netAcceleration = netAcceleration.add(addAcceleration);
						}
						break;
					}
				}
				
				//VELOCITY CAP
				if(rigidbody2DComponent.velocity.lengthSquared() > (rigidbody2DComponent.maxVelocity * rigidbody2DComponent.maxVelocity))
				{
					rigidbody2DComponent.velocity.set(rigidbody2DComponent.velocity.normalize().multiply(rigidbody2DComponent.maxVelocity));
				}

				//POSITION
				transform2DComponent.transform.position.set(transform2DComponent.transform.position.add(rigidbody2DComponent.velocity.multiply(delta)));
				rigidbody2DComponent.acceleration.set(netAcceleration);
			}
		}
	
//COLLISIONS
		if (this.getList("colliders").size() > 1 && enableCollision)
		{
			//iterate through colliders
			for(int a = 0; a < this.getList("colliders").size() - 1; a++)
			{
				int entityIdA = this.getList("colliders").get(a);
				Transform2DComponent transform2DComponentA = world.getComponent(entityIdA, Transform2DComponent.class);
				Collider2DComponent collider2DComponentA = world.getComponent(entityIdA, Collider2DComponent.class);
				Vector2f positionA = transform2DComponentA.transform.getMatrix().transform(collider2DComponentA.position);
				Vector2f lastPositionA = transform2DComponentA.lastTransform.getMatrix().transform(collider2DComponentA.position);

				//iterate through comparing colliders
				for(int b = a + 1; b < this.getList("colliders").size(); b++)
				{
					CollisionData collisionData = new CollisionData();
					
					int entityIdB = this.getList("colliders").get(b);
					Transform2DComponent transform2DComponentB = world.getComponent(entityIdB, Transform2DComponent.class);
					Collider2DComponent collider2DComponentB = world.getComponent(entityIdB, Collider2DComponent.class);				
					Vector2f positionB = transform2DComponentB.transform.getMatrix().transform(collider2DComponentB.position);
					Vector2f lastPositionB = transform2DComponentB.lastTransform.getMatrix().transform(collider2DComponentB.position);
					
//					System.out.println(entityIdA + " vs " + entityIdB);
					
					//sort collisions by collider shape
					int shapeKeyComposite = collider2DComponentA.getShapeKey() | collider2DComponentB.getShapeKey();
					//circle x circle collision
					if(shapeKeyComposite == (this.shapeKeyCircle | this.shapeKeyCircle))
					{
						collisionData = collisionTestCircleCircle.test(lastPositionA, positionA, collider2DComponentA.getShape(Collider2DShapeCircle.class), lastPositionB, positionB, collider2DComponentB.getShape(Collider2DShapeCircle.class), distanceBuffer);
					}
					//point x circle collision
					if(shapeKeyComposite == (this.shapeKeyPoint | this.shapeKeyCircle))
					{
						if(collider2DComponentA.getShapeKey() == this.shapeKeyPoint)
							collisionData = collisionTestPointCircle.test(lastPositionA, positionA, collider2DComponentA.getShape(Collider2DShapePoint.class), lastPositionB, positionB, collider2DComponentB.getShape(Collider2DShapeCircle.class), distanceBuffer);
						else
							collisionData = collisionTestPointCircle.test(lastPositionB, positionB, collider2DComponentB.getShape(Collider2DShapePoint.class), lastPositionA, positionA, collider2DComponentA.getShape(Collider2DShapeCircle.class), distanceBuffer);
					}

//RESOLUTION
					Rigidbody2DComponent rigidbody2DComponentA = world.getComponent(entityIdA, Rigidbody2DComponent.class);
					Rigidbody2DComponent rigidbody2DComponentB = world.getComponent(entityIdB, Rigidbody2DComponent.class);
					
					if(collisionData.collided)
					{
						//SLIDE
						Vector2f slideDirection = new Vector2f(collisionData.collisionNormal.y, -collisionData.collisionNormal.x);
						float slideTime = (1 - collisionData.collisionTime) * delta;
						
						if(collisionData.collisionTime <= 0)
						{
							Vector2f collisionPositionA = getCollisionPosition(transform2DComponentA, collisionData.collisionTime);
							Vector2f collisionPositionB = getCollisionPosition(transform2DComponentB, collisionData.collisionTime);						

//							//TMP
//							System.out.println(collisionPositionA.x + "," + collisionPositionA.y);
//							System.out.println(collisionPositionB.x + "," + collisionPositionB.y);
//							transform2DComponentA.transform.position.set(collisionPositionA);
//							transform2DComponentB.transform.position.set(collisionPositionB);
						}
						
						Vector2f slideVelocityA = getSlideVelocity(rigidbody2DComponentA, slideDirection);
						Vector2f slideVelocityB = getSlideVelocity(rigidbody2DComponentB, slideDirection);
						
						if(collisionData.collisionTime != 0)
						{
							rigidbody2DComponentA.lastVelocity.set(slideVelocityA);
							rigidbody2DComponentB.lastVelocity.set(slideVelocityB);
						}
//						
//						//TMP
//						rigidbody2DComponentA.velocity.set(0, 0);
//						rigidbody2DComponentB.velocity.set(0, 0);

						transform2DComponentA.lastCollisionNormal = collisionData.collisionNormal;
						transform2DComponentB.lastCollisionNormal = collisionData.collisionNormal.multiply(-1);
						
						this.sendMessage(new Message(entityIdA, "HIT", entityIdB));
						this.sendMessage(new Message(entityIdB, "HIT", entityIdA));
					}
					
				}
			}
		}
		
	}

	
	private Vector2f getCollisionPosition(Transform2DComponent transform2DComponent, float collisionTime)
	{
		Vector2f movement = transform2DComponent.transform.position.subtract(transform2DComponent.lastTransform.position);
		return transform2DComponent.lastTransform.position.add(movement.multiply(collisionTime));
	}
	
	private Vector2f getSlideVelocity(Rigidbody2DComponent rigidbody2DComponent, Vector2f slideDirection)
	{
		Vector2f slideVelocity = GeometryUtils.getPointLineProjection(slideDirection, rigidbody2DComponent.velocity);
		if(slideVelocity.lengthSquared() < 0.000001)
		{
			slideVelocity.set(0, 0);
		}
		
		//SPEED CAP
		if(slideVelocity.lengthSquared() > rigidbody2DComponent.maxVelocity * rigidbody2DComponent.maxVelocity)
		{
			slideVelocity.set(slideVelocity.normalize().multiply(rigidbody2DComponent.maxVelocity));
		}
		
		return slideVelocity;
	}
	
}
