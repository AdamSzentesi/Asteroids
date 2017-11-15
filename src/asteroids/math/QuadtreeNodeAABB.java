package asteroids.math;

public class QuadtreeNodeAABB
{
	public Vector2f min;
	public Vector2f max;
	public int value;
	
	public QuadtreeNodeAABB(Vector2f min, Vector2f max, int value)
	{
		this.min = min;
		this.max = max;
		this.value = value;
	}
}