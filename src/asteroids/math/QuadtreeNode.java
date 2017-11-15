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
	private List<Physics2DAABB> objects;
	private List<Integer> entities;
	private List<Integer> indices;
	private QuadtreeBounds bounds;
	private boolean occupied;
	
	public QuadtreeNode(int level, int MAX_OBJECTS, int MAX_LEVEL, QuadtreeBounds bounds)
	{
//		System.out.println(this.getClass().getSimpleName() + ": created on level " + level);
		this.level = level;
		this.MAX_OBJECTS = MAX_OBJECTS;
		this.MAX_LEVEL = MAX_LEVEL;
		this.nodes = new QuadtreeNode[4];
		this.objects = new ArrayList<>();
		this.entities = new ArrayList<>();
		this.indices = new ArrayList<>();
		this.bounds = bounds;
		this.occupied = false;
	}
	
	public void insert(Physics2DAABB box, int entityId)
	{
		int index = getIndex(box);
		
		//early exit: object is on the edge
		if(index == -1)
		{
//			System.out.println(box.hashCode() + " is on the edge of level " + level);
			this.objects.add(box);
			this.entities.add(entityId);
			this.indices.add(index);
			return;
		}
		
		//early exit: this branch is on the maximal level
		if(this.level == this.MAX_LEVEL)
		{
//			System.out.println(box.hashCode() + " is on level " + level + " (" + index + ")");
			this.objects.add(box);
			this.entities.add(entityId);
			this.indices.add(index);
			this.occupied = true;
			return;
		}
		
		//early exit: this branch is not full yet
		if(this.objects.size() < this.MAX_OBJECTS && this.nodes[0] == null)
		{
//			System.out.println(box.hashCode() + " is on level " + level + " (" + index + ")");
			this.objects.add(box);
			this.entities.add(entityId);
			this.indices.add(index);
			this.occupied = true;
			return;
		}
		
		if(this.nodes[0] == null)
		{
//			System.out.println(box.hashCode() + " split " + level + " (" + index + ")");
			split();
			redistribute();		
		}

		this.nodes[index].insert(box, entityId);
	}
	
	private void split()
	{
//		System.out.println(this.getClass().getSimpleName() + ": split on level " + this.level);
		float halfWidth = this.bounds.getWidth() / 2f;
		float halfHeight = this.bounds.getHeight() / 2f;
		
		nodes[0] = new QuadtreeNode(this.level + 1, MAX_OBJECTS, MAX_LEVEL, new QuadtreeBounds(this.bounds.min.add(new Vector2f(0, halfHeight)), halfWidth, halfHeight));
		nodes[1] = new QuadtreeNode(this.level + 1, MAX_OBJECTS, MAX_LEVEL, new QuadtreeBounds(this.bounds.min.add(new Vector2f(halfWidth, halfHeight)), halfWidth, halfHeight));
		nodes[2] = new QuadtreeNode(this.level + 1, MAX_OBJECTS, MAX_LEVEL, new QuadtreeBounds(this.bounds.min.add(new Vector2f(halfWidth, 0)), halfWidth, halfHeight));
		nodes[3] = new QuadtreeNode(this.level + 1, MAX_OBJECTS, MAX_LEVEL, new QuadtreeBounds(this.bounds.min, halfWidth, halfHeight));
	}
	
	private void redistribute()
	{
		int i = 0;
		int index;
		while(i < this.objects.size())
		{
			index = this.indices.get(i);
			if(index != -1)
			{
				this.nodes[index].insert(this.objects.remove(i), this.entities.remove(i));
				this.indices.remove(i);
			}
			else
			{
				i++;
			}
		}
		this.occupied = false;
//		System.out.println(this.level + " " + this.occupied);
	}
	
	public List<Physics2DAABB> getOccupiedList()
	{
		List<Physics2DAABB> result = new ArrayList<>();
		
		if(this.nodes[0] != null)
		{
			for(QuadtreeNode node : this.nodes)
			{
				result.addAll(node.getOccupiedList());
			}
		}
		if(this.occupied)
		{
			result.add(this.objects.get(0));
		}
		return result;
	}
	
	public List getHierarchyList(Physics2DAABB box)
	{
		List<Physics2DAABB> result = new ArrayList<>();

		int index = getIndex(box);
		if(index != -1 && this.nodes[0] != null)
		{
			result.addAll(this.nodes[index].getHierarchyList(box));
		}
		
		result.addAll(this.objects);
		
		return result;
	}
	
	public List<Integer> getEntityOccupiedList()
	{
		List<Integer> result = new ArrayList<>();
		
		if(this.nodes[0] != null)
		{
			for(QuadtreeNode node : this.nodes)
			{
				result.addAll(node.getEntityOccupiedList());
			}
		}
		if(this.occupied)
		{
			result.add(this.entities.get(0));
		}
		return result;
	}
	
	public List<Integer> getEntitiesList(Physics2DAABB box)
	{
		List<Integer> result = new ArrayList<>();

		int index = getIndex(box);
		if(index != -1 && this.nodes[0] != null)
		{
			result.addAll(this.nodes[index].getEntitiesList(box));
		}
		
		result.addAll(this.entities);
		
		return result;
	}
	
	public int getOccupiedCount()
	{
		int result = 0;
		if(this.nodes[0] != null)
		{
			for(QuadtreeNode node : this.nodes)
			{
				result += node.getOccupiedCount();
			}
		}
		if(this.occupied)
		{
			result += 1;
		}
		return result;
	}
	
	public int getIndex(Physics2DAABB box)
	{
		int result = -1;
		int minIndex = getPointIndex(box.min);
		int maxIndex = getPointIndex(box.max);
		if(minIndex == maxIndex)
		{
			result = minIndex;
		}
//		System.out.println(this.getClass().getSimpleName() + ": object on index " + result);
		return result;
	}
	
	private int getPointIndex(Vector2f point)
	{
		int result = -1;
		
		//left quadrants
		if(point.x < (this.bounds.min.x + this.bounds.getWidth() / 2f))
		{
			if(point.y > (this.bounds.min.y + this.bounds.getHeight()/ 2f))
			{
				result = 0;
			}
			if(point.y < (this.bounds.min.y + this.bounds.getHeight()/ 2f))
			{
				result = 3;
			}
		}
		//right quadrants
		else if(point.x > (this.bounds.min.x + this.bounds.getWidth() / 2f))
		{
			if(point.y > (this.bounds.min.y + this.bounds.getHeight()/ 2f))
			{
				result = 1;
			}
			if(point.y < (this.bounds.min.y + this.bounds.getHeight()/ 2f))
			{
				result = 2;
			}
		}
		
		return result;
	}
	
	public void clear()
	{
		Arrays.fill(this.nodes, null);
	}
}
