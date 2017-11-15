package asteroids;

import java.util.ArrayList;
import java.util.List;

public class EntityManager
{
	private int MAX_ENTITIES;
	private long[] entityKeys;
	private int nextEntityId;
	private List<Integer> freeEntityId;
	
	public EntityManager(int MAX_ENTITIES)
	{
		this.MAX_ENTITIES = MAX_ENTITIES;
		this.entityKeys = new long[MAX_ENTITIES];
		this.nextEntityId = 0;
		this.freeEntityId = new ArrayList<>();
	}
	
	public int createEntity()
	{
		int result = -1;
		if(this.nextEntityId < this.MAX_ENTITIES)
		{
			result = this.nextEntityId;
//		System.out.println("EntityManager: entity created: " + id);
			this.entityKeys[result] = 0;
			this.nextEntityId++;			
		}
		else if(this.freeEntityId.size() > 0)
		{
			result = this.freeEntityId.remove(0);
		}
		return result;
	}
	
	public void destroyEntity(int entityId)
	{
		//set entity key to 0
		this.entityKeys[entityId] = 0;
		this.freeEntityId.add(entityId);
	}
	
//	public int getNextEntityId()
//	{
//		return this.nextEntityId;
//	}
	
	public long addEntityKey(int entityId, long entityKey)
	{
		this.entityKeys[entityId] |= entityKey;
		return this.entityKeys[entityId];
	}
	
	public long getEntityKey(int entityId)
	{
		return this.entityKeys[entityId];
	}
	
}
