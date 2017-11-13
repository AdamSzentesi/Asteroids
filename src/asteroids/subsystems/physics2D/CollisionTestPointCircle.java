package asteroids.subsystems.physics2D;

import asteroids.components.Collider.Shapes.Collider2DShapeCircle;
import asteroids.components.Collider.Shapes.Collider2DShapePoint;
import asteroids.math.Vector2f;

public class CollisionTestPointCircle
{
	public CollisionData test(Vector2f pointLastPosition, Vector2f pointPosition, Collider2DShapePoint pointCollider, Vector2f circleLastPosition, Vector2f circlePosition, Collider2DShapeCircle circleCollider, float distanceBuffer)
	{
		CollisionData result = new CollisionData();
		
		float totalRadius = circleCollider.radius + distanceBuffer;
		
		Vector2f pathA = circlePosition.subtract(circleLastPosition);
		Vector2f pathB = pointPosition.subtract(pointLastPosition);
		Vector2f totalPath = pathA.add(pathB.multiply(-1));
		
		Vector2f pathNormalized = totalPath.normalize();
		Vector2f betweenCircles = pointLastPosition.subtract(circleLastPosition);
		
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
