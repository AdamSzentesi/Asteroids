package asteroids.components.Geometry3D.Render3DMesh;

import java.util.ArrayList;
import java.util.HashMap;
import asteroids.math.IntPair;
import asteroids.math.Vector2f;
import asteroids.math.Vector3f;

public class IndexedMesh
{
	private ArrayList<Vertex> vertices = new ArrayList<>();
	private ArrayList<Integer> indices = new ArrayList<>();
	
	public IndexedMesh(String fileName)
	{
		//split the file extension
		int dotIndex = fileName.lastIndexOf('.');
		String fileExtension = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
		
		//check the file extension and prepare parser
		ModelParser modelParser = new ModelParser();
		switch(fileExtension)
		{
			case "obj":
				modelParser = new OBJParser("./res/models/" + fileName);
			break;
			default:
				System.err.println("Error: OBJ file extension is wrong: " + fileExtension);
				System.exit(1);
			break;
		}
	
		//remove double positions
		HashMap<Vector3f, Integer> positionMap = new HashMap<>();
		for(int i = 0; i < modelParser.getIndices().size(); i++)
		{
			int currentPositionIndex = modelParser.getIndices().get(i).vertexIndex;
			Vector3f currentPosition = modelParser.getPositionCoordinates().get(currentPositionIndex);
			Integer oldIndex = positionMap.get(currentPosition);
			if(oldIndex == null)
			{
				positionMap.put(currentPosition, currentPositionIndex);
			}
			else
			{
				int index = modelParser.getIndices().get(i).vertexIndex;
				//System.out.println("replacing: " + index + " with " + oldIndex);
				modelParser.getIndices().get(i).vertexIndex = oldIndex;
			}
		}
		
		//check ridges
		HashMap<IntPair, HashMap<IntPair, Integer>> ridgeMap2 = new HashMap<>();
		int[] indexHelper = new int[]{1, 2, 0};
		int[] indexHelper2 = new int[]{2, 0, 1};
		for(int i = 0; i < modelParser.getIndices().size(); i = i + 3)
		{
			for(int j = 0; j < 3; j++)
			{
				OBJIndex currentIndexA = modelParser.getIndices().get(i + j);
				OBJIndex currentIndexB = modelParser.getIndices().get(i + indexHelper[j]);
				IntPair currentPositions = new IntPair(currentIndexA.vertexIndex, currentIndexB.vertexIndex);
				HashMap<IntPair, Integer> savedLine = ridgeMap2.get(new IntPair(currentIndexB.vertexIndex, currentIndexA.vertexIndex));
				if(savedLine != null)
				{
					savedLine.put(new IntPair(currentIndexB.normalIndex, currentIndexA.normalIndex), i);
					//System.out.println("found:  " + currentIndexB.vertexIndex + ";" + currentIndexA.vertexIndex + " - " + currentIndexB.normalIndex + ";" + currentIndexA.normalIndex + ", total: " + savedLine.size());
					if(savedLine.size() == 2)
					{
						Vector3f finalNormal = new Vector3f();
						for (int k : savedLine.values())
						{
							OBJIndex indexA = modelParser.getIndices().get(k);
							OBJIndex indexB = modelParser.getIndices().get(k + 1);
							OBJIndex indexC = modelParser.getIndices().get(k + 2);
							Vector3f positionA = modelParser.getPositionCoordinates().get(indexA.vertexIndex);
							Vector3f positionB = modelParser.getPositionCoordinates().get(indexB.vertexIndex);
							Vector3f positionC = modelParser.getPositionCoordinates().get(indexC.vertexIndex);
							Vector3f vectorC = positionC.subtract(positionB);
							Vector3f vectorA = positionA.subtract(positionB);
							finalNormal = finalNormal.add(vectorC.cross(vectorA).normalize());
							
						}
						currentIndexA.ridge.set(finalNormal.normalize());
					}
				}
				else
				{
					IntPair currentNormals = new IntPair(currentIndexA.normalIndex, currentIndexB.normalIndex);
					ridgeMap2.put(currentPositions, new HashMap<IntPair, Integer>());//
					ridgeMap2.get(currentPositions).put(currentNormals, i);
					//System.out.println("adding: " + currentPositions.a + ";" + currentPositions.b + " - " + currentNormals.a + ";" + currentNormals.b + ", total: " + ridgeMap2.get(currentPositions).size());
				}
			}
		}
		
		//filter unused indices
		int t = 0;
		HashMap<OBJIndex, Integer> indexMap = new HashMap<>();
		for(int i = 0; i < modelParser.getIndices().size(); i++)
		{
			OBJIndex index = modelParser.getIndices().get(i);
			Integer currentIndex = indexMap.get(index);
			if(currentIndex == null)
			{
				//System.out.println("adding index");
				indexMap.put(index, t);
				
				Vector3f position = modelParser.getPositionCoordinates().get(index.vertexIndex);
				Vector2f texture = modelParser.getTextureCoordinates().get(index.textureCoordinateIndex);
				Vector3f normal = modelParser.getNormalCoordinates().get(index.normalIndex);
				
				//System.out.println(index.vertexIndex + "," + index.normalIndex + "," + index.textureCoordinateIndex + " > " + index.ridge);
				this.vertices.add(new Vertex(position, texture, normal, new Vector3f(), index.ridge));
				this.indices.add(t);
				t++;
			}
			else
			{
				//System.out.println(modelParser.getPositionCoordinates().get(index.vertexIndex).x + "," + modelParser.getPositionCoordinates().get(index.vertexIndex).y + "," + modelParser.getPositionCoordinates().get(index.vertexIndex).z + " > " + index.ridge);
				//System.out.println(index.vertexIndex + "," + index.normalIndex + "," + index.textureCoordinateIndex + " > " + index.ridge);
				this.indices.add(currentIndex);
			}
		}
				
		calcTangents();
	}
	
	private void calcTangents()
	{
		for(int i = 0; i < this.getIndexCount(); i = i + 3)
		{
			int i0 = this.indices.get(i);
			int i1 = this.indices.get(i + 1);
			int i2 = this.indices.get(i + 2);
			
			Vector3f edge1 = this.vertices.get(i1).positionCoordinates.subtract(this.vertices.get(i0).positionCoordinates);
			Vector3f edge2 = this.vertices.get(i2).positionCoordinates.subtract(this.vertices.get(i0).positionCoordinates);
			
			float deltaU1 = this.vertices.get(i1).textureCoordinates.getX() - this.vertices.get(i0).textureCoordinates.getX();
			float deltaV1 = this.vertices.get(i1).textureCoordinates.getY() - this.vertices.get(i0).textureCoordinates.getY();
			float deltaU2 = this.vertices.get(i2).textureCoordinates.getX() - this.vertices.get(i0).textureCoordinates.getX();
			float deltaV2 = this.vertices.get(i2).textureCoordinates.getY() - this.vertices.get(i0).textureCoordinates.getY();
			
			float dividend = (deltaU1*deltaV2 - deltaU2*deltaV1);
			//TODO: The first 0.0f may need to be changed to 1.0f here.
			float f = dividend == 0 ? 0.0f : 1.0f/dividend;
			
			Vector3f tangent = new Vector3f(0,0,0);
			tangent.setX(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()));
			tangent.setY(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()));
			tangent.setZ(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()));
			
			this.vertices.get(i0).tangent.set(tangent);
			this.vertices.get(i1).tangent.set(tangent);
			this.vertices.get(i2).tangent.set(tangent);
		}
	}
		
	public int getVertexCount()
	{
		return this.vertices.size();
	}
	
	public int getIndexCount()
	{
		return this.indices.size();
	}
	
	public ArrayList<Vertex> getVertices()
	{
		return this.vertices;
	}
	
	public ArrayList<Integer> getIndices()
	{
		return this.indices;
	}
	
	public Vertex getVertex(int i)
	{
		return this.vertices.get(i);
	}
}
