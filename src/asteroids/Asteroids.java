/**
 *  ██▀▀█▄  ██▀▀▀▀   ▄██▄   ██▀█▄   ██  ██
 *  ██▄▄█▀  ██▄▄    ██▄▄██  ██  ██  ▀█▄▄█▀
 *  ██▀█▄   ██      ██  ██  ██ ▄█▀    ██      ▄▄
 *  ▀▀  ▀▀  ▀▀▀▀▀▀  ▀▀  ▀▀  ▀▀▀▀      ▀▀      ▀▀
 * ████████
 * ████████
 * ████████
 * ████████
 * 
 * (C) Copyright READY GAMES 2017, GPL Licence
 */
package asteroids;

/**
 * Main class
 * 
 * @author Adam Szentesi
 */
public class Asteroids
{
	public static void main(String[] args)
	{
		CoreEngine coreEngine = new CoreEngine(1024, 768, 60, "Clonoids");
		coreEngine.start();
	}
	
}
