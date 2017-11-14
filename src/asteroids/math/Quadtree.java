package asteroids.math;

import asteroids.subsystems.physics2D.Physics2DAABB;
import java.util.List;

public class Quadtree
{
	private final int MAX_OBJECTS;
	private final int MAX_LEVEL;
	private final QuadtreeNode rootNode;
	
	public Quadtree(int MAX_OBJECTS, int MAX_LEVEL, Vector2f min, Vector2f max)
	{
		this.MAX_OBJECTS = MAX_OBJECTS;
		this.MAX_LEVEL = MAX_LEVEL;
		this.rootNode = new QuadtreeNode(0, MAX_OBJECTS, MAX_LEVEL, new QuadtreeBounds(min, max));
	}
	
	public void insert(Physics2DAABB box)
	{
		this.rootNode.insert(box);
	}
	
	public int getOccupiedCount()
	{
		return this.rootNode.getOccupiedCount();
	}
	
	public List<Physics2DAABB> getOccupiedList()
	{
		return this.rootNode.getOccupiedList();
	}
	
	public List<Physics2DAABB> getHierarchyList(Physics2DAABB box)
	{
		return this.rootNode.getHierarchyList(box);
	}
	
	public void clear()
	{
		this.rootNode.clear();
	}
}
