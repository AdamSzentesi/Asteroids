package asteroids.geometry;

import asteroids.math.Vector2f;
import asteroids.math.Vector3f;

public class GeometryUtils
{
	
	public static float getTriangleRayIntersectionDistance(Vector3f orig, Vector3f dir, Vector3f v0, Vector3f v1, Vector3f v2)
	{
		// orig and dir defines the ray. v0, v1, v2 defines the triangle.
		// returns the distance from the ray origin to the intersection or 0.

		Vector3f e1 = v1.subtract(v0);
		Vector3f e2 = v2.subtract(v0);
		// Calculate planes normal vector
		Vector3f pvec = dir.cross(e2);
		float det = e1.dot(pvec);

		// Ray is parallel to plane
		if (det < 1e-8 && det > -1e-8)
		{
			return 0;
		}

		float inv_det = 1 / det;
		Vector3f tvec = orig.subtract(v0);
		float u = tvec.dot(pvec) * inv_det;
		if (u < 0 || u > 1)
		{
			return 0;
		}

		Vector3f qvec = tvec.cross(e1);
		float v = dir.dot(qvec) * inv_det;
		if (v < 0 || u + v > 1)
		{
			return 0;
		}
		
		return e2.dot(qvec) * inv_det;
	}

	public static float getPointSegmentDistance(Vector2f start, Vector2f end, Vector2f point)
	{
		float l2 = getSquaredDistance(start, end);  // i.e. |w-v|^2 -  avoid a sqrt
		//segment has length 0
		Vector2f sp = point.subtract(start);
		if (l2 == 0.0)
		{
			return sp.length();
		}
		Vector2f tangent = end.subtract(start);
		float c = sp.dot(tangent);
		float t = Math.max(0, Math.min(1, c / l2));
		Vector2f projection = start.add(tangent.multiply(t));  // Projection falls on the segment
		return projection.subtract(point).length();
	}
	
	
	public static float getSquaredDistance(Vector2f start, Vector2f end)
	{
		return (float)(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));
	}
	
	public static Vector2f getSegmentSegmentIntersection(Vector2f segmentAStart, Vector2f segmentAEnd, Vector2f segmentBStart, Vector2f segmentBEnd)
  {
		Vector2f result = null;
		
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
				result = R.multiply(t).add(P1);
			}
		}
    return result;
  }
	
	public static Vector2f getLineLineIntersection(Vector2f lineStartA, Vector2f lineEndA, Vector2f lineStartB, Vector2f lineEndB)
	{
		float dxA = lineEndA.x - lineStartA.x;
		float dyA = lineEndA.y - lineStartA.y;
		float dxB = lineEndB.x - lineStartB.x;
		float dyB = lineEndB.y - lineStartB.y;
		
		float x = 0;
		float y = 0;
		
		//early exit: both are vertical
		if(dxA == 0 && dxB == 0)
		{
			return null;
		}
		
		if(dxA == 0)
		{
			float slopeB = dyB / dxB;
			float shiftB = lineStartB.y - slopeB * lineStartB.x;
			x = lineStartA.x;
			y = slopeB * x + shiftB;
			return new Vector2f(x, y);
		}
		
		if(dxB == 0)
		{
			float slopeA = dyA / dxA;
			float shiftA = lineStartA.y - slopeA * lineStartA.x;
			x = lineStartB.x;
			y = slopeA * x + shiftA;
			return new Vector2f(x, y);
		}
		
		float slopeA = dyA / dxA;
		float slopeB = dyB / dxB;
		
		float shiftA = lineStartA.y - slopeA * lineStartA.x;
		float shiftB = lineStartB.y - slopeB * lineStartB.x;
		
		x = (shiftB - shiftA) / (slopeA - slopeB);
		y = slopeA * x + shiftA;
				
		return new Vector2f(x, y);
	}
	
	public static Vector2f getPointLineProjection(Vector2f lineStart, Vector2f lineEnd, Vector2f point)
	{
		Vector2f line = lineEnd.subtract(lineStart).normalize();
		Vector2f toPoint = point.subtract(lineStart);
		float d = toPoint.dot(line);
		return lineStart.add(line.multiply(d));
	}
	
	public static Vector2f getPointLineProjection(Vector2f lineEnd, Vector2f point)
	{
		Vector2f line = lineEnd.normalize();
		float d = point.dot(line);
		return line.multiply(d);
	}
}
