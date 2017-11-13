package asteroids.components.Geometry3D.Render3DMesh;

import java.util.ArrayList;
import asteroids.math.Vector2f;
import asteroids.math.Vector3f;

public class ModelParser
{
	private ArrayList<Vector3f> positionCoordinates = new ArrayList<>();
	private ArrayList<Vector2f> textureCoordinates = new ArrayList<>();
	private ArrayList<Vector3f> normalCoordinates = new ArrayList<>();
	private ArrayList<OBJIndex> indices = new ArrayList<>();
	
	public ArrayList<Vector3f> getPositionCoordinates()
	{
		return this.positionCoordinates;
	}
	
	public ArrayList<Vector2f> getTextureCoordinates()
	{
		return this.textureCoordinates;
	}
	
	public ArrayList<Vector3f> getNormalCoordinates()
	{
		return this.normalCoordinates;
	}
	
	public ArrayList<OBJIndex> getIndices()
	{
		return this.indices;
	}

}
