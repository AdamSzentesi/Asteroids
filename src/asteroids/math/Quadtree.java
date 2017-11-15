package asteroids.math;

import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.codegen.CompilerConstants;

public class Quadtree
{
	private final int MAX_OBJECTS;
	private int level = 0;
	private List<QuadtreeNode> nodes;
	private Quadtree NW;
	private Quadtree NE;
	private Quadtree SE;
	private Quadtree SW;
	private QuadtreeBounds bounds;

	public Quadtree(int level, int MAX_OBJECTS, Vector2f leftBottomCorner, float width, float height)
	{
		this.level = level;
		this.MAX_OBJECTS = MAX_OBJECTS;
		this.bounds = new QuadtreeBounds(leftBottomCorner, width, height);
		this.nodes = new ArrayList<>();
	}
	
	public Quadtree(int MAX_OBJECTS, Vector2f leftBottomCorner, float width, float height)
	{
		this(0, MAX_OBJECTS, leftBottomCorner, width, height);
	}
	
	public void insert(Vector2f point, int value)
	{
//		System.out.println("insert " + point.x + "," + point.y);
		if(!this.bounds.inInside(point)) return;
		
		QuadtreeNode node = new QuadtreeNode(point.x, point.y, value);
		
		if(this.nodes.size() < this.MAX_OBJECTS)
		{
			this.nodes.add(node);
			return;
		}
		
		split();
		
		if(this.NE.bounds.inInside(point)) this.NE.insert(point, value);
		else if(this.NW.bounds.inInside(point)) this.NW.insert(point, value);
		else if(this.SE.bounds.inInside(point)) this.SE.insert(point, value);
		else if(this.SW.bounds.inInside(point)) this.SW.insert(point, value);
		else System.out.println("ERROR: Unhandled partition " + point.x + "," + point.y);
	}
	
	public static void search(Quadtree quadtree)
	{
		if (quadtree == null) return;
		
		System.out.println("Level = " + quadtree.level + " :" + quadtree.bounds.min.x + "," + quadtree.bounds.min.y + " > " + quadtree.bounds.max.x + "," + quadtree.bounds.max.y);
		if(quadtree.nodes.isEmpty())
		{
			System.out.println(".leaf");
		}
		else
		{
			for (QuadtreeNode node : quadtree.nodes)
			{
				System.out.println(". " + node.value);
			}
		}
		
		search(quadtree.NE);
		search(quadtree.NW);
		search(quadtree.SE);
		search(quadtree.SW);
	}
	
//	public static List getList(NewQuadtree quadtree)
//	{
//		List result = new ArrayList<Integer>();
//		List result2 = new ArrayList<ArrayList<Integer>>();
//		
//		if(!quadtree.nodes.isEmpty())
//		{
//			System.out.println("pack");
//			List pack = new ArrayList<Integer>();
//			for (NewQuadtreeNode node : quadtree.nodes)
//			{
//				result.add(node.value);
//				pack.add(node.value);
//				System.out.println(". " + node.value);
//			}
//			result2.add(pack);
//		}
//		
//		if (quadtree.NE != null)
//		{
//			result.addAll(getList(quadtree.NE));
//			result.addAll(getList(quadtree.NW));
//			result.addAll(getList(quadtree.SE));
//			result.addAll(getList(quadtree.SW));
//		}
//		
//		return result;
//	}

	public static List getFullList(Quadtree quadtree)
	{
		List result = new ArrayList<ArrayList<Integer>>();
		
		if(!quadtree.nodes.isEmpty())
		{
//			System.out.println("pack");
			List pack = new ArrayList<Integer>();
			for (QuadtreeNode node : quadtree.nodes)
			{
				pack.add(node.value);
//				System.out.println(". " + node.value);
			}
			result.add(pack);
		}
		
		if (quadtree.NE != null)
		{
			result.addAll(getFullList(quadtree.NE));
			result.addAll(getFullList(quadtree.NW));
			result.addAll(getFullList(quadtree.SE));
			result.addAll(getFullList(quadtree.SW));
		}
		
		return result;
	}
	
	private void split()
	{
		if(this.NE != null) return;
		
		float halfWidth = this.bounds.getWidth() / 2f;
		float halfHeight = this.bounds.getHeight() / 2f;
		
		this.NE = new Quadtree(this.level + 1, MAX_OBJECTS, this.bounds.min.add(new Vector2f(0, halfHeight)), halfWidth, halfHeight);
		this.NW = new Quadtree(this.level + 1, MAX_OBJECTS, this.bounds.min.add(new Vector2f(halfWidth, halfHeight)), halfWidth, halfHeight);
		this.SE = new Quadtree(this.level + 1, MAX_OBJECTS, this.bounds.min.add(new Vector2f(halfWidth, 0)), halfWidth, halfHeight);
		this.SW = new Quadtree(this.level + 1, MAX_OBJECTS, this.bounds.min, halfWidth, halfHeight);
	}
}
