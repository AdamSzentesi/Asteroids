package asteroids.math;

import asteroids.subsystems.physics2D.Physics2DAABB;
import java.util.List;

public class _Quadtree
{
	private final int MAX_OBJECTS;
	private final int MAX_LEVEL;
	private final _QuadtreeNode rootNode;
	
	public _Quadtree(int MAX_OBJECTS, int MAX_LEVEL, Vector2f min, Vector2f max)
	{
		this.MAX_OBJECTS = MAX_OBJECTS;
		this.MAX_LEVEL = MAX_LEVEL;
		this.rootNode = new _QuadtreeNode(0, MAX_OBJECTS, MAX_LEVEL, new QuadtreeBounds(min, max));
	}
	
	public void insert(Physics2DAABB box, int entityId)
	{
		this.rootNode.insert(box, entityId);
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
	
	public List<Integer> getEntityOccupiedList()
	{
		return this.rootNode.getEntityOccupiedList();
	}
	
	public List<Integer> getEntitiesList(Physics2DAABB box)
	{
		return this.rootNode.getEntitiesList(box);
	}
}
