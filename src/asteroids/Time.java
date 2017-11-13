package asteroids;

public class Time
{
	private static final long SECOND = 1000000000L; //one second in nanoseconds
	private static double delta;
	
	public static double getTime()
	{
		return (double)System.nanoTime()/(double)SECOND;
		
	}
	
}
