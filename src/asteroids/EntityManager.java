package asteroids;

public class EntityManager
{
	private int MAX_ENTITIES;
	private long[] entityKeys;
	private int nextEntityId;
	
	public EntityManager(int MAX_ENTITIES)
	{
		this.MAX_ENTITIES = MAX_ENTITIES;
		this.entityKeys = new long[MAX_ENTITIES];
		this.nextEntityId = 0;
	}
	
	public int createEntity()
	{
		int id = this.nextEntityId;
//		System.out.println("EntityManager: entity created: " + id);
		this.entityKeys[id] = 0;
		this.nextEntityId++;
		return id;
	}
	
	public void destroyEntity(int entityId)
	{
		//set entity key to 0
		this.entityKeys[entityId] = 0;
	}
	
	public int getNextEntityId()
	{
		return this.nextEntityId;
	}
	
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
