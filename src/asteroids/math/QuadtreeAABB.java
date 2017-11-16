package asteroids.math;

import asteroids.subsystems.physics2D.Physics2DAABB;
import java.util.ArrayList;
import java.util.List;

public class QuadtreeAABB
{
	private final int MAX_OBJECTS;
	private final int MAX_DEPTH;
	private int level = 0;
	private List<QuadtreeNodeAABB> nodes;
	private QuadtreeAABB NW;
	private QuadtreeAABB NE;
	private QuadtreeAABB SE;
	private QuadtreeAABB SW;
	private QuadtreeBoundsAABB bounds;

	public QuadtreeAABB(int level, int MAX_OBJECTS, int MAX_DEPTH, Vector2f leftBottomCorner, float width, float height)
	{
		this.level = level;
		this.MAX_OBJECTS = MAX_OBJECTS;
		this.MAX_DEPTH = MAX_DEPTH;
		this.bounds = new QuadtreeBoundsAABB(leftBottomCorner, width, height);
		this.nodes = new ArrayList<>();
	}
	
	public QuadtreeAABB(int MAX_OBJECTS, int MAX_DEPTH, Vector2f leftBottomCorner, float width, float height)
	{
		this(0, MAX_OBJECTS, MAX_DEPTH, leftBottomCorner, width, height);
	}
	
	public void insert(Physics2DAABB box, int value)
	{
		if(!this.bounds.isInside(box)) return;
		
		QuadtreeNodeAABB node = new QuadtreeNodeAABB(box.min, box.max, value);
		
		//System.out.println(value + "level " + this.level + " > " + this.MAX_DEPTH);
		if(this.nodes.size() < this.MAX_OBJECTS || this.level <= this.MAX_DEPTH)
		{
			this.nodes.add(node);
			return;
		}

		split();
		for(QuadtreeNodeAABB nodeToRedistribute : this.nodes)
		{
			distribute(new Physics2DAABB(nodeToRedistribute.min, nodeToRedistribute.max), nodeToRedistribute.value);
//			if(this.level > 10)
//			{
//				System.out.println(value + "leveln " + this.level);
//			}
		}
		this.nodes.clear();
		
		distribute(box, value);
	}
	
	private void distribute(Physics2DAABB box, int value)
	{
		if(this.NE.bounds.isInside(box)) this.NE.insert(box, value);
		if(this.NW.bounds.isInside(box)) this.NW.insert(box, value);
		if(this.SE.bounds.isInside(box)) this.SE.insert(box, value);
		if(this.SW.bounds.isInside(box)) this.SW.insert(box, value);
		//else System.out.println("ERROR: Unhandled partition " + box.min + "," + box.max);
	}

	public static List getFullList(QuadtreeAABB quadtree)
	{
		List result = new ArrayList<ArrayList<Integer>>();
		
		if(!quadtree.nodes.isEmpty())
		{
//			System.out.println("pack " + quadtree.level);
			List pack = new ArrayList<Integer>();
			for (QuadtreeNodeAABB node : quadtree.nodes)
			{
				pack.add(node.value);
//				System.out.print(". " + node.value);
			}
//			System.out.println("");
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
		
		this.NE = new QuadtreeAABB(this.level + 1, MAX_OBJECTS, this.bounds.min.add(new Vector2f(0, halfHeight)), halfWidth, halfHeight);
		this.NW = new QuadtreeAABB(this.level + 1, MAX_OBJECTS, this.bounds.min.add(new Vector2f(halfWidth, halfHeight)), halfWidth, halfHeight);
		this.SE = new QuadtreeAABB(this.level + 1, MAX_OBJECTS, this.bounds.min.add(new Vector2f(halfWidth, 0)), halfWidth, halfHeight);
		this.SW = new QuadtreeAABB(this.level + 1, MAX_OBJECTS, this.bounds.min, halfWidth, halfHeight);
	}
}
