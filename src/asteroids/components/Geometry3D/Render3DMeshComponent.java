package asteroids.components.Geometry3D;

import asteroids.components.Component;
import asteroids.components.Geometry3D.Render3DMesh.MeshLoader;
import asteroids.math.Vector3f;
import asteroids.components.Geometry3D.Render3DMesh.Material;

public class Render3DMeshComponent extends Component
{
	public int vbo;
	public int vboCount;
	public int ibo;
	public int iboCount;
	
	public Vector3f min;
	public Vector3f max;
	
	public Material material;
	
	public Render3DMeshComponent()
	{
		//default vertex data
		float[] vertexArray =
		{
			-0.5f, 0.0f, 0.5f,			0.0f, 0.0f,		0.0f, 0.0f, 0.0f,		0.0f, 0.0f, 0.0f,		0.0f, 0.0f, 0.0f,
			0.5f, 0.0f, 0.5f,				0.0f, 0.0f,		0.0f, 0.0f, 0.0f,		0.0f, 0.0f, 0.0f,		0.0f, 0.0f, 0.0f,
			0.0f, 0.0f, -0.5f,			0.0f, 0.0f,		0.0f, 0.0f, 0.0f,		0.0f, 0.0f, 0.0f,		0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f,				0.0f, 0.0f,		0.0f, 1.0f, 0.0f,		0.0f, 0.0f, 0.0f,		0.0f, 0.0f, 0.0f,
		};
		//default index data
		int[] indexArray =
		{
			2, 1, 0,
			0, 1, 3,
			3, 1, 2,
			0, 3, 2,
		};
		loadMesh(vertexArray, indexArray);
	}
	
	public void loadMesh(float[] vertexArray, int[] indexArray)
	{
		MeshLoader meshLoader = new MeshLoader(vertexArray, indexArray);

		this.vbo = meshLoader.getVbo();
		this.vboCount = meshLoader.getVboCount();
		this.ibo = meshLoader.getIbo();
		this.iboCount = meshLoader.getIboCount();
		
		this.min = meshLoader.min;
		this.max = meshLoader.max;
	}
	
	public void loadMesh(String fileName)
	{
		MeshLoader meshLoader = new MeshLoader(fileName);

		this.vbo = meshLoader.getVbo();
		this.vboCount = meshLoader.getVboCount();
		this.ibo = meshLoader.getIbo();
		this.iboCount = meshLoader.getIboCount();
		
		this.min = meshLoader.min;
		this.max = meshLoader.max;
	}
	
	public void setMaterial(Material material)
	{
		this.material = material;
	}
}
