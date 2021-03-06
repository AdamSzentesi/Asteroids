package asteroids.subsystems;

import asteroids.Message;
import asteroids.World;
import asteroids.components.Collider.Collider2DComponent;
import asteroids.components.Collider.Shapes.Collider2DShapeCircle;
import asteroids.components.Collider.Shapes.Collider2DShapePoint;
import asteroids.components.Geometry2D.Rigidbody2DComponent;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.geometry.GeometryUtils;
import asteroids.math.Grid;
import asteroids.math.Matrix4f;
import asteroids.math.Vector2f;
import static asteroids.messenger.Messages.*;
import asteroids.subsystems.physics2D.*;
import java.util.ArrayList;
import java.util.List;

public class Physics2DCollisionSubsystem extends Subsystem
{
	private final float distanceBuffer = 0.01f;
	private Grid grid = new Grid(16, new Vector2f(-2, -2), 4, 4);
	
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
	
	private List<Long> collisionIgnoreList = new ArrayList<>();
	
	@Override
	public void process(World world, float delta)
	{
		//grid
		this.grid.clear();
		for(int entityId : this.getPrimaryList())
		{
			Transform2DComponent transform2DComponent = world.getComponent(entityId, Transform2DComponent.class);
			Matrix4f rotateScaleMatrix = transform2DComponent.getWorldMatrix();//no
			Physics2DAABB colliderAABB = world.getComponent(entityId, Collider2DComponent.class).getAABB(rotateScaleMatrix);
			Vector2f firstPosition = transform2DComponent.lastTransform.position;
			Vector2f secondPosition = transform2DComponent.transform.position;
			Physics2DAABB box = getMotionAABB(colliderAABB, firstPosition, secondPosition);
			
//			Matrix4f worldMatrix = world.getComponent(entityId, Transform2DComponent.class).getWorldMatrix();
//			Physics2DAABB box = getMotionAABB2(colliderAABB, firstPosition, secondPosition);
			grid.insert(box, entityId);
		}
		
		List<List<Integer>> cycle = grid.getFullList();
		for(List<Integer> collisionList : cycle)
		{
			collide(collisionList, world, delta);
		}
//		grid.debug();
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
					//TODO: collision groups
					int entityIdB = colliders.get(b);
					boolean ignore = false;
					
					for(long ignoredKey : this.collisionIgnoreList)
					{
						if(world.hasEntityComponent(entityIdA, ignoredKey) && world.hasEntityComponent(entityIdB, ignoredKey))
						{
							ignore = true;
						}
					}
					
					if(ignore == false)
					{
						Transform2DComponent transform2DComponentB = world.getComponent(entityIdB, Transform2DComponent.class);
						Collider2DComponent collider2DComponentB = world.getComponent(entityIdB, Collider2DComponent.class);				
						Vector2f positionB = transform2DComponentB.transform.getMatrix().transform(collider2DComponentB.position);
						Vector2f lastPositionB = transform2DComponentB.lastTransform.getMatrix().transform(collider2DComponentB.position);

						CollisionData collisionData = new CollisionData();
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

							this.sendMessage(new Message(entityIdA, ECS_HIT, entityIdB));
							this.sendMessage(new Message(entityIdB, ECS_HIT, entityIdA));
						
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
	
	public void addIgnoreKey(long key)
	{
		this.collisionIgnoreList.add(key);
		System.out.println(this.getClass().getSimpleName() + ": added ignored collision between objects with: " + key);
	}
	
	/**
	 * Returns an AABB of a collider expanded with movement vector
	 * TODO: optimize for "if not moving" case
	 * 
	 * @param  colliderAABB
	 * @param  firstPosition
	 * @param  secondPosition
	 * @return 
	 */
	private Physics2DAABB getMotionAABB(Physics2DAABB colliderAABB, Vector2f firstPosition, Vector2f secondPosition)
	{
		float minX = Math.min(firstPosition.x, secondPosition.x);
		float minY = Math.min(firstPosition.y, secondPosition.y);
		float maxX = Math.max(firstPosition.x, secondPosition.x);
		float maxY = Math.max(firstPosition.y, secondPosition.y);
		
		Vector2f min = new Vector2f(minX, minY).add(colliderAABB.min);
		Vector2f max = new Vector2f(maxX, maxY).add(colliderAABB.max);
		
//		System.out.println("col " + colliderAABB.min.x + "," + colliderAABB.min.y + " max " + colliderAABB.max.x + "," + colliderAABB.max.y);
//		System.out.println("mov " + firstPosition.x + "," + firstPosition.y + " --- " + secondPosition.x + "," + secondPosition.y);
//		System.out.println("fin " + min.x + "," + min.y + " --- " + max.x + "," + max.y);
		
		return new Physics2DAABB(min, max);
	}
}
