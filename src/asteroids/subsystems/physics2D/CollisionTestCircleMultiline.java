package asteroids.subsystems.physics2D;

import asteroids.components.Collider.Shapes.Collider2DShapeCircle;
import asteroids.components.Collider.Shapes.Collider2DShapeLine;
import asteroids.components.Collider.Shapes.Collider2DShapeMultiline;
import asteroids.math.Pair;
import asteroids.math.Vector2f;

public class CollisionTestCircleMultiline
{
	private CollisionTestCircleLine collisionTestCircleLine = new CollisionTestCircleLine();

	public CollisionData test(Vector2f circleLastPosition, Vector2f circlePosition, Collider2DShapeCircle circleShape, Vector2f multilinePosition, Collider2DShapeMultiline multilineShape, float distanceBuffer)
	{
		CollisionData result = new CollisionData();
		for(Pair<Vector2f, Vector2f> line : multilineShape.lines)
		{
			result = collisionTestCircleLine.test2(circleLastPosition, circlePosition, circleShape, multilinePosition, new Collider2DShapeLine(line.a, line.b), distanceBuffer);
			if(result.collided)
			{
				return result;
			}
		}
		return result;
	}
}
