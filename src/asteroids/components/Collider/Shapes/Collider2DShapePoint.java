package asteroids.components.Collider.Shapes;

import asteroids.math.Vector2f;
import asteroids.subsystems.render2D.primitives.Debug2DPrimitiveLine;

public class Collider2DShapePoint extends Collider2DShape
{
	public Collider2DShapePoint()
	{
		this.debug2DPrimitive = new Debug2DPrimitiveLine(new Vector2f(), new Vector2f(0.0f, -0.05f));
		this.shapeKey = 1 << 5;
	}

}
