package asteroids.subsystems.physics2D;

import asteroids.components.Collider.Shapes.Collider2DShapeLine;
import asteroids.components.Collider.Shapes.Collider2DShapeMultiline;
import asteroids.math.Pair;
import asteroids.math.Vector2f;

public class CollisionTestLineMultiline
{
	private CollisionTestLineLine collisionTestLineLine = new CollisionTestLineLine();
	
	public boolean test(Vector2f linePosition, Collider2DShapeLine lineShape, Vector2f multilinePosition, Collider2DShapeMultiline multilineShape)
	{
		for(Pair<Vector2f, Vector2f> line : multilineShape.lines)
		{
			if(collisionTestLineLine.test(linePosition, lineShape, multilinePosition, new Collider2DShapeLine(line.a, line.b)))
			{
				return true;
			}
		}
		return false;
	}

}
