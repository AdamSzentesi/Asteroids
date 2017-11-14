package asteroids.math;

import asteroids.subsystems.physics2D.Physics2DAABB;

public class Quadtree
{
	private final int MAX_OBJECTS;
	private final int MAX_LEVEL;
	private final QuadtreeNode rootNode;
	
	public Quadtree(int MAX_OBJECTS, int MAX_LEVEL)
	{
		this.MAX_OBJECTS = MAX_OBJECTS;
		this.MAX_LEVEL = MAX_LEVEL;
		this.rootNode = new QuadtreeNode(0, MAX_OBJECTS, MAX_LEVEL);
	}
	
	public void insert(Physics2DAABB box)
	{
		this.rootNode.insert(box);
	}
	
	public void clear()
	{
		this.rootNode.clear();
	}
}
