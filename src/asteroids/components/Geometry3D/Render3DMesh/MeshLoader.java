package asteroids.components.Geometry3D.Render3DMesh;

import asteroids.Util;
import asteroids.math.Vector3f;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL15.*;

public class MeshLoader
{
	public int vbo;
	public int vboCount;
	public int ibo;
	public int iboCount;
	
	public Vector3f min = new Vector3f();
	public Vector3f max = new Vector3f();
	
	public MeshLoader(float[] vertexArray, int[] indexArray)
	{
		addVertices(vertexArray, indexArray);
	}
	
	public MeshLoader(String fileName)
	{
		//get parsed and indexed mesh data
		IndexedMesh indexedMesh = new IndexedMesh(fileName);
		
		System.out.println("  + parsing indexed:");
		System.out.println("  |- vertices: " + indexedMesh.getVertexCount());
		System.out.println("  |- indices: " + indexedMesh.getIndexCount());
		
		//fill the arrays for buffering
		float[] vertexArray = new float[indexedMesh.getVertexCount() * Vertex.SIZE];
		Integer[] indexArray0 = new Integer[indexedMesh.getIndexCount()];
		indexedMesh.getIndices().toArray(indexArray0);

		int[] indexArray = Util.toIntArray(indexArray0);
		
		for(int i = 0; i < indexedMesh.getVertexCount(); i++)
		{
			vertexArray[i * Vertex.SIZE] = indexedMesh.getVertex(i).positionCoordinates.x;
			vertexArray[i * Vertex.SIZE + 1] = indexedMesh.getVertex(i).positionCoordinates.y;
			vertexArray[i * Vertex.SIZE + 2] = indexedMesh.getVertex(i).positionCoordinates.z;
			
			vertexArray[i * Vertex.SIZE + 3] = indexedMesh.getVertex(i).textureCoordinates.x;
			vertexArray[i * Vertex.SIZE + 4] = indexedMesh.getVertex(i).textureCoordinates.y;

			vertexArray[i * Vertex.SIZE + 5] = indexedMesh.getVertex(i).normal.x;
			vertexArray[i * Vertex.SIZE + 6] = indexedMesh.getVertex(i).normal.y;
			vertexArray[i * Vertex.SIZE + 7] = indexedMesh.getVertex(i).normal.z;
			
			vertexArray[i * Vertex.SIZE + 8] = indexedMesh.getVertex(i).tangent.x;
			vertexArray[i * Vertex.SIZE + 9] = indexedMesh.getVertex(i).tangent.y;
			vertexArray[i * Vertex.SIZE + 10] = indexedMesh.getVertex(i).tangent.z;
			
			vertexArray[i * Vertex.SIZE + 11] = indexedMesh.getVertex(i).ridge.x;
			vertexArray[i * Vertex.SIZE + 12] = indexedMesh.getVertex(i).ridge.y;
			vertexArray[i * Vertex.SIZE + 13] = indexedMesh.getVertex(i).ridge.z;
		}

//		for(float index : vertexArray)
//		{
//			System.out.print(index + "|");
//		}

		//buffering
		addVertices(vertexArray, indexArray);
	}
	
	private void addVertices(float[] vertexArray, int[] indexArray)
	{
		//find default AABB min and max
		for(int i = 0; i < vertexArray.length; i = i + Vertex.SIZE)
		{
			if(vertexArray[i] > this.max.x){this.max.x = vertexArray[i];}
			if(vertexArray[i] < this.min.x){this.min.x = vertexArray[i];}
			if(vertexArray[i + 1] > this.max.y){this.max.y = vertexArray[i + 1];}
			if(vertexArray[i + 1] < this.min.y){this.min.y = vertexArray[i + 1];}
			if(vertexArray[i + 2] > this.max.z){this.max.z = vertexArray[i + 2];}
			if(vertexArray[i + 2] < this.min.z){this.min.z = vertexArray[i + 2];}
		}
		
		//turn vertex array to buffer
		FloatBuffer vertexBuffer = Util.makeFlippedBuffer(vertexArray);
		vbo = makeVBO(vertexBuffer);
		vboCount = vertexArray.length; 
		
		//turn index array to buffer
		IntBuffer indexBuffer = Util.makeFlippedBuffer(indexArray);
		ibo = makeIBO(indexBuffer);
		iboCount = indexArray.length;
	}
	
	public int getVbo()
	{
		return this.vbo;
	}
	
	public int getVboCount()
	{
		return this.vboCount;
	}
	
	public int getIbo()
	{
		return this.ibo;
	}
	
	public int getIboCount()
	{
		return this.iboCount;
	}
	
	//return VBO from FloatBuffer
	private int makeVBO(FloatBuffer inputData)
	{
		int vertexBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
			glBufferData(GL_ARRAY_BUFFER, inputData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return vertexBuffer;
	}
	
	//return IBO from FloatBuffer
	private int makeIBO(IntBuffer inputData)
	{
		int indexBuffer = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, inputData, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		return indexBuffer;
	}
	
}
