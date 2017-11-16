package asteroids.subsystems;

import asteroids.Message;
import asteroids.World;
import asteroids.components.AsteroidComponent;
import asteroids.components.Collider.Collider2DComponent;
import asteroids.components.Collider.Shapes.Collider2DShapeCircle;
import asteroids.components.Collider.Shapes.Collider2DShapePoint;
import asteroids.components.Geometry2D.Rigidbody2DComponent;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.geometry.GeometryUtils;
import asteroids.math.QuadtreeAABB;
import asteroids.math.Vector2f;
import asteroids.subsystems.physics2D.CollisionData;
import asteroids.subsystems.physics2D.CollisionTestCircleCircle;
import asteroids.subsystems.physics2D.CollisionTestCircleLine;
import asteroids.subsystems.physics2D.CollisionTestCircleMultiline;
import asteroids.subsystems.physics2D.CollisionTestLineLine;
import asteroids.subsystems.physics2D.CollisionTestLineMultiline;
import asteroids.subsystems.physics2D.CollisionTestPointCircle;
import asteroids.subsystems.physics2D.Physics2DAABB;
import java.util.List;

public class Physics2DCollisionSubsystem extends Subsystem
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
	public void process(World world, float delta)
	{
		long time0 = System.nanoTime();
		QuadtreeAABB quadtree = new QuadtreeAABB(4, 20, new Vector2f(-2, -2), 4, 4);
		for(int entityId : this.getList("primary"))
		{
			Physics2DAABB aabb = world.getComponent(entityId, Collider2DComponent.class).aabb;
			Vector2f position = world.getComponent(entityId, Transform2DComponent.class).transform.position;
			Physics2DAABB box = new Physics2DAABB(aabb.min.add(position), aabb.max.add(position));
			quadtree.insert(box, entityId);
		}
//		System.out.println("time 1: " + (System.nanoTime() - time0));
		
		List<List<Integer>> cycle = QuadtreeAABB.getFullList(quadtree);
		for(List<Integer> collisionList : cycle)
		{
			collide(collisionList, world, delta);
		}
	}
	
	private void collide(List<Integer> colliders, World world, float delta)
	{
		if (colliders.size() > 1)
		{
			//iterate through colliders
			for(int a = 0; a < colliders.size() - 1; a++)
			{
				int entityIdA = colliders.get(a);
				Transform2DComponent transform2DComponentA = world.getComponent(entityIdA, Transform2DComponent.class);
				Collider2DComponent collider2DComponentA = world.getComponent(entityIdA, Collider2DComponent.class);
				Vector2f positionA = transform2DComponentA.transform.getMatrix().transform(collider2DComponentA.position);
				Vector2f lastPositionA = transform2DComponentA.lastTransform.getMatrix().transform(collider2DComponentA.position);

				//iterate through comparing colliders
				for(int b = a + 1; b < colliders.size(); b++)
				{
					int entityIdB = colliders.get(b);
					if(world.hasEntityComponent(entityIdA, AsteroidComponent.class) && world.hasEntityComponent(entityIdB, AsteroidComponent.class))
					{
					}
					else
					{
						CollisionData collisionData = new CollisionData();

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
