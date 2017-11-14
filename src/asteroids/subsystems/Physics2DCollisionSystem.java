package asteroids.subsystems;

import asteroids.World;

public class Physics2DCollisionSystem extends Subsystem
{
	boolean collisions = true;
	
	@Override
	public void process(World world, float delta)
	{
		if (this.getList("colliders").size() > 1 && collisions)
		{
			//iterate through colliders
			for(int a = 0; a < this.getList("colliders").size() - 1; a++)
			{
				//iterate through comparing colliders
				for(int b = a + 1; b < this.getList("colliders").size(); b++)
				{
					
				}
			}
		}
	}
}
