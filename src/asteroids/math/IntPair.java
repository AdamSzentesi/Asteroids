package asteroids.math;

public class IntPair
{
	public int a;
	public int b;
	
	public IntPair(int a, int b)
	{
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean equals(Object object)
	{
		IntPair intPair = (IntPair)object;
		return a == intPair.a && b == intPair.b;
	}
	
	@Override
	public int hashCode()
	{
		final int BASE = 17;
		final int MULTIPLIER = 31;
		int result = BASE;
		
		result = MULTIPLIER * result + a;
		result = MULTIPLIER * result + b;
	
		return result;
	}
}
