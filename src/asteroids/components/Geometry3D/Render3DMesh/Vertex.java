package asteroids.components.Geometry3D.Render3DMesh;

import asteroids.math.Vector2f;
import asteroids.math.Vector3f;

public class Vertex
{
	public static final int SIZE = 14;
	
	public Vector3f positionCoordinates;
	public Vector2f textureCoordinates;
	public Vector3f normal;
	public Vector3f tangent;
	public Vector3f ridge;
	
	public Vertex(Vector3f positionCoordinates,	Vector2f textureCoordinates, Vector3f normal, Vector3f tangent, Vector3f ridge)
	{
		this.positionCoordinates = positionCoordinates;
		this.textureCoordinates = textureCoordinates;
		this.normal = normal;
		this.tangent = tangent;
		this.ridge = ridge;
	}
}
