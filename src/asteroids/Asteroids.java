package asteroids;

public class Asteroids
{
	public static void main(String[] args)
	{
		CoreEngine coreEngine = new CoreEngine(1024, 768, 60, "Asteroids");
		coreEngine.start();
	}
	
}
