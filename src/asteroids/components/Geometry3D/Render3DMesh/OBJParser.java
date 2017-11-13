package asteroids.components.Geometry3D.Render3DMesh;

import asteroids.Util;
import asteroids.math.Vector2f;
import asteroids.math.Vector3f;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class OBJParser extends ModelParser
{
	private ArrayList<Vector3f> positionCoordinates = new ArrayList<>();
	private ArrayList<Vector2f> textureCoordinates = new ArrayList<>();
	private ArrayList<Vector3f> normalCoordinates = new ArrayList<>();
	private ArrayList<OBJIndex> indices = new ArrayList<>();
	
	public OBJParser(String fileName)
	{
		try
		{
			BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
			String line;
			
			while((line = fileReader.readLine()) != null)
			{
				//get text file as a String array
				String[] tokens = line.split(" ");
				tokens = Util.removeEmptyStrings(tokens);
				
				if(tokens.length == 0 || tokens[0].equals("#"))
					continue;
				else if(tokens[0].equals("v"))
				{
					positionCoordinates.add(new Vector3f(Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2]),
							Float.valueOf(tokens[3])));
				}
				else if(tokens[0].equals("vt"))
				{
					textureCoordinates.add(new Vector2f(Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2])));
				}
				else if(tokens[0].equals("vn"))
				{
					normalCoordinates.add(new Vector3f(Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2]),
							Float.valueOf(tokens[3])));
				}
				else if(tokens[0].equals("f"))
				{
					//triangulation
					for(int i = 0; i < tokens.length - 3; i++)
					{
						indices.add(parseOBJIndex(tokens[1]));
						indices.add(parseOBJIndex(tokens[2 + i]));
						indices.add(parseOBJIndex(tokens[3 + i]));
					}
				}
			}
			fileReader.close();
			
			System.out.println("  + parsing OBJ:");
			System.out.println("  |- vertices: " + positionCoordinates.size());
			if(this.hasTextureCoordinates()) System.out.println("  |- textureCoordinates " + textureCoordinates.size());
			if(this.hasNormalCoordinates()) System.out.println("  |- normals " + normalCoordinates.size());
			System.out.println("  |- indices " + indices.size());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	private OBJIndex parseOBJIndex(String token)
	{
		String[] values = token.split("/");

		OBJIndex result = new OBJIndex();
		result.vertexIndex = Integer.parseInt(values[0]) - 1;

		if(values.length > 1)
		{
			if(!values[1].isEmpty())
			{
				result.textureCoordinateIndex = Integer.parseInt(values[1])-1;
			}
			if(values.length > 2)
			{
				result.normalIndex = Integer.parseInt(values[2]) - 1;
			}
		}
		int currentIndex = this.indices.size();
		//this.ridgeMap.put(Integer.SIZE, Integer.MIN_VALUE)
		
		return result;
	}
	
	public boolean hasTextureCoordinates()
	{
		if(this.textureCoordinates.size() > 0)
		{
			return true;
		}
		return false;
	}
	
	public boolean hasNormalCoordinates()
	{
		if(this.normalCoordinates.size() > 0)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public ArrayList<Vector3f> getPositionCoordinates()
	{
		return this.positionCoordinates;
	}
	
	@Override
	public ArrayList<Vector2f> getTextureCoordinates()
	{
		return this.textureCoordinates;
	}
	
	@Override
	public ArrayList<Vector3f> getNormalCoordinates()
	{
		return this.normalCoordinates;
	}
	
	public ArrayList<Integer> getIndicesCount()
	{
		ArrayList<Integer> result = new ArrayList<>();
		for(int i = 0; i < this.indices.size(); i++)
		{
			result.add(this.indices.get(i).vertexIndex);
		}
		return result;
	}
	
	@Override
	public ArrayList<OBJIndex> getIndices()
	{
		return this.indices;
	}
}