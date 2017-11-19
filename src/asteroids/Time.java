package asteroids;

public class Time
{
	private static final long SECOND = 1000000000L; //one second in nanoseconds
	private static double delta;
	
	public static double getTime()
	{
		return System.nanoTime();
		
	}
	
	public static double getMicroTime()
	{
		return (double)System.nanoTime()/(double)SECOND;
	}
}
