package asteroids.subsystems.physics2D;

import asteroids.components.Collider.Shapes.Collider2DShapeCircle;
import asteroids.components.Collider.Shapes.Collider2DShapeLine;
import asteroids.geometry.GeometryUtils;
import asteroids.math.Vector2f;

public class CollisionTestCircleLine
{
	public CollisionData test(Vector2f circleLastPosition, Vector2f circlePosition, Collider2DShapeCircle circleShape, Vector2f linePosition, Collider2DShapeLine lineShape, float distanceBuffer)
	{
		CollisionData result = new CollisionData();

		//circle travel vector
		Vector2f a = circlePosition.subtract(circleLastPosition);
		//early exit: no movement
		if(a.lengthSquared() == 0)
		{
			return result;
		}
		
		//line vector
		Vector2f lineStart = lineShape.startPoint.add(linePosition);
		Vector2f lineEnd = lineShape.endPoint.add(linePosition);
		//nearest point on line
		Vector2f y = GeometryUtils.getPointLineProjection(lineStart, lineEnd, circleLastPosition);
		Vector2f ay = circleLastPosition.subtract(y);
		Vector2f reflectionNormal = ay.normalize();
		Vector2f traverseNormalised = a.normalize();
		float dot = traverseNormalised.dot(reflectionNormal);
		//early exit: moving away from each other
		if(dot > 0)
		{
			return result;
		}
		
		//intersection X
		Vector2f x = GeometryUtils.getLineLineIntersection(circleLastPosition, circlePosition, lineStart, lineEnd);
		//vector from intersection to circle
		Vector2f ax = circleLastPosition.subtract(x);
		float axLength = ax.length();
		float rest = axLength * ((circleShape.radius + distanceBuffer) / ay.length());
		
		if(axLength > (a.length() + rest))
		{
			return result;
		}
		
		float distance = GeometryUtils.getPointSegmentDistance(lineStart, lineEnd, circlePosition);
		if(distance > (circleShape.radius + distanceBuffer))
		{
			return result;
		}

//		System.out.println("circleLastPosition " + circleLastPosition.x + "," + circleLastPosition.y);
//		System.out.println("circlePosition   " + circlePosition.x + "," + circlePosition.y);
//		System.out.println("lineStart " + lineStart.x + "," + lineStart.y);
//		System.out.println("lineEnd   " + lineEnd.x + "," + lineEnd.y);
//		System.out.println("x " + x.x + "," + x.y);
//		System.out.println("a " + a.length());
//		System.out.println("ax " + axLength);
//		System.out.println("y " + y.x + "," + y.y);
//		System.out.println("ay  " + ay.length());		
		
		float finalTravel = axLength - rest;
		result.collided = true;
		result.collisionNormal = reflectionNormal;
		return result;
	}
	
	public CollisionData test2(Vector2f circleLastPosition, Vector2f circlePosition, Collider2DShapeCircle circleShape, Vector2f linePosition, Collider2DShapeLine lineShape, float distanceBuffer)
	{
		CollisionData result = new CollisionData();
		
		//early exit: no movement
		if(circleLastPosition == circlePosition)
		{
			return result;
		}

		//line vector
		Vector2f lineStart = lineShape.startPoint.add(linePosition);
		Vector2f lineEnd = lineShape.endPoint.add(linePosition);
		
		//get normal towards the circle
		Vector2f projection = GeometryUtils.getPointLineProjection(lineStart, lineEnd, circleLastPosition);
		Vector2f lineNormal = circleLastPosition.subtract(projection).normalize();

		//movement vector
		Vector2f movement = circlePosition.subtract(circleLastPosition);

		//early exit: moving away from each other
		//System.out.println(movement.dot(lineNormal));
		if(movement.normalize().dot(lineNormal) > 0)
		{
			return result;
		}
		
		//early exit: inside distance buffer TODO: OPTIMIZE!!!!!
		float distance = GeometryUtils.getPointSegmentDistance(lineStart, lineEnd, circleLastPosition);
		if(distance < distanceBuffer + circleShape.radius)
		{
			result.collided = true;
			result.collisionNormal = lineNormal;
//			System.out.println("normal:" + lineNormal.x + "," + lineNormal.y);
			result.collisionTime = 0;
			return result;
		}

		Vector2f normalOffset = lineNormal.multiply(circleShape.radius + distanceBuffer / 2);

		Vector2f intersection = GeometryUtils.getSegmentSegmentIntersection(lineStart.add(normalOffset), lineEnd.add(normalOffset), circleLastPosition, circlePosition);
		//early exit: no segment intersection
		if(intersection == null)
		{
			return result;
		}
		
		Vector2f collisionMovement = intersection.subtract(circleLastPosition);
		float collisionTime = (float)Math.sqrt(collisionMovement.lengthSquared() / movement.lengthSquared());
		
		result.collided = true;
		result.collisionNormal = lineNormal;
		result.collisionTime = collisionTime;

//		System.out.println(circleLastPosition.x + "," + circleLastPosition.y + " > " + circlePosition.x + "," + circlePosition.y);
//		System.out.println(lineStart.x + "," + lineStart.y + " > " + lineEnd.x + "," + lineEnd.y);
//		System.out.println("normal:" + normalOffset.x + "," + normalOffset.y);
//		System.out.println(collisionTime);

		return result;
	}
}
