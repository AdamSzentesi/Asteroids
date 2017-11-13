package asteroids.components.Geometry3D.Render3DMesh;

import asteroids.math.Vector3f;

public class OBJIndex
{
	public int vertexIndex;
	public int textureCoordinateIndex;
	public int normalIndex;
	public Vector3f ridge = new Vector3f(0.0f, 0.0f, 0.0f);
	public boolean ridgePresent = false;
	
	@Override
	public boolean equals(Object object)
	{
		OBJIndex index = (OBJIndex)object;
		return vertexIndex == index.vertexIndex
						&& textureCoordinateIndex == index.textureCoordinateIndex
						&& normalIndex == index.normalIndex
						&& ridge == index.ridge
						&& ridgePresent == index.ridgePresent;
	}
	
	@Override
	public int hashCode()
	{
		final int BASE = 17;
		final int MULTIPLIER = 31;
		int result = BASE;
		
		result = MULTIPLIER * result + vertexIndex;
		result = MULTIPLIER * result + textureCoordinateIndex;
		result = MULTIPLIER * result + normalIndex;
		result = MULTIPLIER * result + (ridgePresent ? 1 : 0);
		
		return result;
	}
}
