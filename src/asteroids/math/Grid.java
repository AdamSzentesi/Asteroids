package asteroids.math;

import asteroids.subsystems.physics2D.Physics2DAABB;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grid
{
	private final int level;
	private List<Integer>[][] cells;
	private Vector2f min;
	private float width;
	private float height;
	private float cellSizeX;
	private float cellSizeY;
	
	//TODO: change data structure to list with only filled sectors
	public Grid(int level, Vector2f bottomLeftCorner, float width, float height)
	{
		this.level = level;
		this.cells = new ArrayList[level][level];
		this.min = bottomLeftCorner;
		this.width = width;
		this.height = height;
		this.cellSizeX = width / level;
		this.cellSizeY = height / level;
	}
	
	public void insert(Physics2DAABB box, int value)
	{
		if(!isInGrid(box)) return;
		
		Vector2f relativeMin = box.min.subtract(this.min);
		Vector2f relativeMax = box.max.subtract(this.min);
		
		float minX = relativeMin.x / this.cellSizeX;
		float minY = relativeMin.y / this.cellSizeY;
		float maxX = relativeMax.x / this.cellSizeX;
		float maxY = relativeMax.y / this.cellSizeY;
		
		int regionMinX = (int)minX;
		if(minX == regionMinX && regionMinX > 0)
		{
			regionMinX--;
		}
		
		int regionMinY = (int)minY;
		if(minY == regionMinY && regionMinY > 0)
		{
			regionMinY--;
		}
		
		int regionMaxX = (int)maxX;
		if(regionMaxX >= this.level)
		{
			regionMaxX = this.level - 1;
		}
		
		int regionMaxY = (int)maxY;
		if(regionMaxY >= this.level)
		{
			regionMaxY = this.level - 1;
		}

		for(int i = regionMinX; i <= regionMaxX; i++)
		{
			for(int j = regionMinY; j <= regionMaxY; j++)
			{
				if(this.cells[i][j] == null)
				{
					this.cells[i][j] = new ArrayList();
				}
				this.cells[i][j].add(value);
			}
		}
	}

	//TODO: optimize, seems clumsy
	private boolean isInGrid(Physics2DAABB box)
	{
		boolean result = false;
		
		float halfWidth = box.getWidth() / 2;
		float halfHeight = box.getHeight() / 2;
		float positionX = box.min.x + halfWidth;
		float positionY = box.min.y + halfHeight;

		if
		(
			positionX >= this.min.x - halfWidth &&
			positionX <= this.min.x + this.width + halfWidth &&
			positionY >= this.min.y - halfHeight &&
			positionY <= this.min.y + this.height + halfHeight
		)
		{
			result = true;
		}
		
		return result;
	}
	
	public List getFullList()
	{
		List<List<Integer>> result = new ArrayList();
		for(int i = 0; i < this.level; i++)
		{
			for(int j = 0; j < this.level; j++)
			{
				if(this.cells[i][j] != null)
				{
					result.add(this.cells[i][j]);
				}
			}
		}
		
		return result;
	}
	
	public void clear()
	{
		for(List[] list : this.cells)
		{
			Arrays.fill(list, null);
		}	
	}

}
