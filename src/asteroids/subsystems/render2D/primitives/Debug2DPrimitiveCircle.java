package asteroids.subsystems.render2D.primitives;

public class Debug2DPrimitiveCircle extends Debug2DPrimitive
{
	public Debug2DPrimitiveCircle(float radius)
	{
		float[] vertexArray = new float[24];
		int[] indexArray = new int[13];
		for(int i = 0; i < 12; i++)
		{
			vertexArray[i * 2] = (float)Math.sin(Math.PI / 12 * i * 2) * radius;
			vertexArray[i * 2 + 1] = (float)Math.cos(Math.PI / 12 * i * 2) * radius;
			indexArray[i] = i;
		}
		indexArray[12] = 0;

		loadLine(vertexArray, indexArray);	
	}
	
}
