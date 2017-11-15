package asteroids.subsystems;

import asteroids.World;
import asteroids.components.Collider.Collider2DComponent;
import asteroids.components.Geometry2D.Transform2DComponent;
import asteroids.math.Quadtree;
import asteroids.math.Vector2f;
import asteroids.subsystems.physics2D.Physics2DAABB;

public class Physics2DCollisionSubsystem extends Subsystem
{
	boolean collisions = true;
	
	@Override
	public void process(World world, float delta)
	{
		//test
		Quadtree quadtree = new Quadtree(2, 30, new Vector2f(-2, -2), new Vector2f(2, 2));
		//fill the quadtree
		for(int entityId : this.getList("primary"))
		{
			Physics2DAABB aabb = world.getComponent(entityId, Collider2DComponent.class).aabb;
			Vector2f position = world.getComponent(entityId, Transform2DComponent.class).transform.position;
			Physics2DAABB insert = new Physics2DAABB(aabb.min.add(position), aabb.max.add(position));
			//TODO: size of AABB + movement vector = sweep AABB!!!
			quadtree.insert(insert, entityId);
		}
		System.out.println("OCCUPIED: " + quadtree.getOccupiedCount());
		for(Physics2DAABB box : quadtree.getOccupiedList())
		{
			System.out.println("colliding group " + box.hashCode());
			for(int collidingEntity : quadtree.getEntitiesList(box))
			{
				System.out.println("- colliding " + collidingEntity);
			}
		}
		
		if (this.getList("primary").size() > 1 && collisions)
		{
			//iterate through colliders
			for(int a = 0; a < this.getList("primary").size() - 1; a++)
			{
				//iterate through comparing colliders
				for(int b = a + 1; b < this.getList("primary").size(); b++)
				{
					
				}
			}
		}
	}
}
