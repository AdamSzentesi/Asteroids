package asteroids.components.Collider.Shapes;

import asteroids.math.Pair;
import asteroids.math.Vector2f;
import asteroids.subsystems.render2D.primitives.Debug2DPrimitiveMultiline;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Collider2DShapeMultiline extends Collider2DShape
{
	public List<Pair<Vector2f, Vector2f>> lines;
	
	public Collider2DShapeMultiline(ArrayList<Pair<Vector2f, Vector2f>> lines)
	{
		this.lines = lines;
		
		float[] vertices = new float[lines.size() * 4];
		int[] indices = new int[lines.size() * 2];
		for(int i = 0; i < lines.size(); i++)
		{
			Vector2f start = lines.get(i).a;
			Vector2f end = lines.get(i).b;
			
			vertices[i * 4] = start.x;
			vertices[i * 4 + 1] = start.y;
			vertices[i * 4 + 2] = end.x;
			vertices[i * 4 + 3] = end.y;
			
			indices[i * 2] = i * 2;
			indices[i * 2 + 1] = i * 2 + 1;
		}
		//this.debug2DPrimitive = new Debug2DPrimitiveMultiline(vertices, indices);
		this.shapeKey = 1 << 3;
	}
	
	public Collider2DShapeMultiline(Pair<Vector2f, Vector2f>[] lines)
	{
		this.lines = Arrays.asList(lines);
		
		float[] vertices = new float[this.lines.size() * 4];
		int[] indices = new int[this.lines.size() * 2];
		for(int i = 0; i < this.lines.size(); i++)
		{
			Vector2f start = this.lines.get(i).a;
			Vector2f end = this.lines.get(i).b;
			
			vertices[i * 4] = start.x;
			vertices[i * 4 + 1] = start.y;
			vertices[i * 4 + 2] = end.x;
			vertices[i * 4 + 3] = end.y;
			
			indices[i * 2] = i * 2;
			indices[i * 2 + 1] = i * 2 + 1;
		}
		//this.debug2DPrimitive = new Debug2DPrimitiveMultiline(vertices, indices);
		this.shapeKey = 1 << 3;
	}
	
}
