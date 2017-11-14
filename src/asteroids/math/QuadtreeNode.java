package asteroids.math;

import asteroids.subsystems.physics2D.Physics2DAABB;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuadtreeNode
{
	private final int level;
	private final int MAX_OBJECTS;
	private final int MAX_LEVEL;
	private QuadtreeNode[] nodes;
	private List objects;
	
	public QuadtreeNode(int level, int MAX_OBJECTS, int MAX_LEVEL)
	{
		this.level = level;
		this.MAX_OBJECTS = MAX_OBJECTS;
		this.MAX_LEVEL = MAX_LEVEL;
		this.nodes = new QuadtreeNode[4];
		this.objects = new ArrayList<>();
	}
	
	public void insert(Physics2DAABB box)
	{
		//early exit: this level is not full yet
		if(this.objects.size() <= this.MAX_OBJECTS)
		{
			this.objects.add(box);
			return;
		}
		
		
	}
	
	private void split()
	{
//		int subWidth = (int)(bounds.getWidth() / 2);
//		int subHeight = (int)(bounds.getHeight() / 2);
//		int x = (int)bounds.getX();
//		int y = (int)bounds.getY();

		nodes[0] = new QuadtreeNode(this.level + 1, MAX_OBJECTS, MAX_LEVEL);
		nodes[1] = new QuadtreeNode(this.level + 1, MAX_OBJECTS, MAX_LEVEL);
		nodes[2] = new QuadtreeNode(this.level + 1, MAX_OBJECTS, MAX_LEVEL);
		nodes[3] = new QuadtreeNode(this.level + 1, MAX_OBJECTS, MAX_LEVEL);
	}
	
	public void insert2()
	{
//		if (objects.size() > this.MAX_OBJECTS && this.level < this.MAX_LEVEL)
//		{
//			if (nodes[0] == null)
//			{ 
//				split(); 
//			}
//			int i = 0;
//			while (i < objects.size())
//			{
//				int index = getIndex(objects.get(i));
//				if (index != -1)
//				{
//					nodes[index].insert(objects.remove(i));
//				}
//				else
//				{
//					i++;
//				}
//			}
//		}
	}
	
	public void clear()
	{
		Arrays.fill(this.nodes, null);
	}
}
