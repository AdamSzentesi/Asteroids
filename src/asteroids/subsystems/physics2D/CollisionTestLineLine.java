package asteroids.subsystems.physics2D;

import asteroids.components.Collider.Shapes.Collider2DShapeLine;
import asteroids.math.Vector2f;

public class CollisionTestLineLine
{
	Vector2f intersectionPoint;
	
	public boolean test(Vector2f linePositionA, Collider2DShapeLine lineShapeA, Vector2f linePositionB, Collider2DShapeLine lineShapeB)
  {
		Vector2f lineStartA = linePositionA.add(lineShapeA.startPoint);
		Vector2f lineEndA = linePositionA.add(lineShapeA.endPoint);
		Vector2f lineStartB = linePositionB.add(lineShapeB.startPoint);
		Vector2f lineEndB = linePositionB.add(lineShapeB.endPoint);
		return testSegments(lineStartA, lineEndA, lineStartB, lineEndB);
  }
	
	public boolean testSegments(Vector2f segmentAStart, Vector2f segmentAEnd, Vector2f segmentBStart, Vector2f segmentBEnd)
  {
		this.intersectionPoint = null;
		boolean result = false;
		
		Vector2f P1 = segmentAStart;
		Vector2f P2 = segmentAEnd;
		Vector2f Q1 = segmentBStart;
		Vector2f Q2 = segmentBEnd;

		Vector2f R = P2.subtract(P1);
		Vector2f S = Q2.subtract(Q1);

		float den = R.cross(S);

		Vector2f num = Q1.subtract(P1);
		float Snum = num.cross(S);
		float Rnum = num.cross(R);

		if(den != 0)
		{
			float t = Snum/den;
			float u = Rnum/den;
			if(Rnum != 0 && t >= 0 && t <= 1 && u >= 0 && u <= 1)
			{
				this.intersectionPoint = R.multiply(t-0.01f).add(P1);
				result = true;
			}
		}
    return result;
  }
	
	public Vector2f getIntersection()
	{
		return this.intersectionPoint;
	}
}
