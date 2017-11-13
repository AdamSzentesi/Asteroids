package asteroids.subsystems.render2D.primitives;

public class Debug2DPrimitiveRectangle extends Debug2DPrimitive
{
	public Debug2DPrimitiveRectangle()
	{
		float[] vertexArray =
		{
			-0.500000f, -0.500000f,
			-0.500000f, 0.500000f,
			0.500000f, 0.500000f,
			0.500000f, -0.500000f
		};
		//default index data
		int[] indexArray =
		{
			0, 1,
			1, 2,
			2, 3,
			3, 0
		};
		loadLine(vertexArray, indexArray);	
	}
	
}
