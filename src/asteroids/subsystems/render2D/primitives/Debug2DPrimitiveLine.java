package asteroids.subsystems.render2D.primitives;

import asteroids.math.Vector2f;

public class Debug2DPrimitiveLine extends Debug2DPrimitive
{
	public Debug2DPrimitiveLine(Vector2f startPoint, Vector2f endPoint)
	{
		float[] vertexArray =
		{
			startPoint.x, startPoint.y,
			endPoint.x, endPoint.y
		};
		//default index data
		int[] indexArray =
		{
			0, 1
		};
		loadLine(vertexArray, indexArray);	
	}
	
}
