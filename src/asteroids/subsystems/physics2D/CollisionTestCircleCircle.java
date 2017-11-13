package asteroids.subsystems.physics2D;

import asteroids.components.Collider.Shapes.Collider2DShapeCircle;
import asteroids.math.Vector2f;

public class CollisionTestCircleCircle
{
	public CollisionData test(Vector2f circleLastPositionA, Vector2f circlePositionA, Collider2DShapeCircle circleColliderA, Vector2f circleLastPositionB, Vector2f circlePositionB, Collider2DShapeCircle circleColliderB, float distanceBuffer)
	{
		CollisionData result = new CollisionData();
		
		float totalRadius = circleColliderA.radius + circleColliderB.radius + distanceBuffer;
		
		Vector2f pathA = circlePositionA.subtract(circleLastPositionA);
		Vector2f pathB = circlePositionB.subtract(circleLastPositionB);
		Vector2f totalPath = pathA.add(pathB.multiply(-1));
		
		Vector2f pathNormalized = totalPath.normalize();
		Vector2f betweenCircles = circleLastPositionB.subtract(circleLastPositionA);
		
		float projection = pathNormalized.dot(betweenCircles);
		//early exit: point moving away from total circle
		if(projection <= 0)
		{
			return result;
		}
		
		float centerFromLineSquared = Math.abs(betweenCircles.lengthSquared() - (projection * projection));
		//early exit: point missing the total circle
		if(centerFromLineSquared > (totalRadius * totalRadius))
		{
			return result;
		}
		
		float distanceSquared = (totalRadius * totalRadius) - centerFromLineSquared;
		float toIntersectionSquared = (projection * projection) - distanceSquared;
		float time = toIntersectionSquared / totalPath.lengthSquared();
		if(time > 1)
		{
			return result;
		}
		
//		System.out.println("COLLISION");
		
		result.collided = true;
		result.collisionTime = time;
		
		return result;
	}
	
}
