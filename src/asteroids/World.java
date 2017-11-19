package asteroids;

import asteroids.components.Component;
import asteroids.math.Pair;
import asteroids.subsystems.Subsystem;
import java.util.ArrayList;

public class World
{
	private final int MAX_ENTITIES;
	private final int MAX_COMPONENTS = 32;
	
	private EntityManager entityManager;
	private SubsystemManager subsystemManager;
	private AbstractComponentManager componentManager;
	
	private ArrayList<Pair<Integer, Long>> entitiesToCreate = new ArrayList<>();
	private ArrayList<Integer> entitiesToDestroy = new ArrayList<>();
	
	public World(final int MAX_ENTITIES)
	{
		this.MAX_ENTITIES = MAX_ENTITIES;
		this.entityManager = new EntityManager(this.MAX_ENTITIES);
		this.subsystemManager = new SubsystemManager();
		this.componentManager = new ComponentManager(this.MAX_COMPONENTS, this.MAX_ENTITIES);
	}
	
	/**
	 * Main world update cycle
	 * 
	 * @param delta delta time
	 */
	public void update(float delta)
	{
		this.createEntities();
		this.subsystemManager.process(this, delta);
		this.destroyEntities();
	}
	
	/**
	 * Main world render cycle
	 * 
	 * @param delta delta time
	 */
	public void render(float delta)
	{
		this.createEntities();
		this.subsystemManager.render(this, delta);
		this.destroyEntities();
	}
	
	/**
	 * Clean-up at exit
	 */
	public void cleanUp()
	{
		//this.entityManager.cleanUp();
		this.subsystemManager.cleanUp();
		//this.componentManager.cleanUp();
	}
	
//COMPONENTS
	/**
	 * Registers a component type in the ComponentManager
	 * 
	 * @param componentClass component class to register
	 * @return bitwise component type key
	 */
	public long registerComponent(Class componentClass)
	{
		return this.componentManager.registerComponent(componentClass);
	}
	
	/**
	 * Creates and adds a new component to given entity
	 * 
	 * @param entityId       entity to add the component to
	 * @param componentClass added component class
	 */
	public void addComponent(int entityId, Class componentClass)
	{
		long componentKey = this.componentManager.addComponent(entityId, componentClass);
		long entityKey = this.entityManager.addEntityKey(entityId, componentKey);
		this.entitiesToCreate.add(new Pair<Integer, Long>(entityId, entityKey));
	}
	
	/**
	 * Retrieves a component of a given class from an entity
	 * 
	 * @param <T>
	 * @param entityId       entity to get the component from
	 * @param componentClass retrieved component class
	 * @return component of given subclass
	 */
	public <T extends Component> T getComponent(int entityId, Class<T> componentClass)
	{
		return this.componentManager.getComponent(entityId, componentClass);
	}
	
	/**
	 * Retrieves a bitwise component type key of a given component class
	 * 
	 * @param componentClass
	 * @return bitwise component type key
	 */
	public long getComponentKey(Class componentClass)
	{
		return this.componentManager.getComponentKey(componentClass);
	}
	
//SUBSYSTEMS
	/**
	 * Registers a subsystem in the SubsystemManager.
	 * <b>WARNING:</b> Order of adding is the order of execution!
	 * 
	 * @param subsystemClass subsystem class to add
	 * @param componentKey   bitwise key of primary index of iterable entities
	 */
	public void addSubsystem(Class subsystemClass, long componentKey)
	{
		this.subsystemManager.addSubsystem(subsystemClass, componentKey);
	}
	public void addRenderSubsystem(Class subsystemClass, long componentKey)
	{
		this.subsystemManager.addRenderSubsystem(subsystemClass, componentKey);
	}
	
	/**
	 * Retrieves a registered subsystem of a given subsystem class
	 * 
	 * @param <T>
	 * @param subsystemClass
	 * @return 
	 */
	public <T extends Subsystem> T getSubsystem(Class<T> subsystemClass)
	{
		return this.subsystemManager.getSubsystem(subsystemClass);
	}
	public <T extends Subsystem> T getRenderSubsystem(Class<T> subsystemClass)
	{
		return this.subsystemManager.getRenderSubsystem(subsystemClass);
	}
	
//ENTITIES
	/**
	 * Creates new entity in the ECS
	 * 
	 * @return entity ID handle
	 */
	public int createEntity()
	{
		int result = this.entityManager.createEntity();
		this.subsystemManager.addMessageBank(result);
		return result;
	}
	
	/**
	 * Destroys given entity
	 * 
	 * @param entityId entity to destroy (ID)
	 */
	public void destroyEntity(int entityId)
	{
		if(!this.entitiesToDestroy.contains(entityId))
		{
			this.entitiesToDestroy.add(entityId);
		}
	}
	
	/**
	 * Retrieves entity bitwise key. Entity key defines which types of components is the entity composed of
	 * 
	 * @param entityId entity ID
	 * @return 
	 */
	public long getEntityKey(int entityId)
	{
		return this.entityManager.getEntityKey(entityId);
	}
	
	/**
	 * Asks if entity has a component of a given type
	 * 
	 * @param entityId       entity ID
	 * @param componentClass component class
	 * @return true if component is part of a given entity
	 */
	public boolean hasEntityComponent(int entityId, Class componentClass)
	{
		long componentKey = this.componentManager.getComponentKey(componentClass);
		return hasEntityComponent(entityId, componentKey);
	}
	
	public boolean hasEntityComponent(int entityId, long componentKey)
	{
		boolean result = false;
		long entityKey = this.entityManager.getEntityKey(entityId);
		if(entityKey == (entityKey | componentKey))
		{
			result = true;
		}
		return result;
	}
	
//AUTOMATION
	/**
	 * Postponed entity creation: entities created during subsystems iterations are dormant and activated only before subsystems cycle
	 */
	private void createEntities()
	{
		for(Pair<Integer, Long> pair : this.entitiesToCreate)
		{
			this.subsystemManager.updateEntityList(pair.a, pair.b);
		}
		this.entitiesToCreate.clear();
	}
	
	/**
	 * Postponed entity destruction: entities destroyed during subsystems iterations are active and erased only after subsystems cycle
	 */
	private void destroyEntities()
	{
		for(int entityId : this.entitiesToDestroy)
		{
			this.entityManager.destroyEntity(entityId);
			this.componentManager.destroyComponentsOnEntity(entityId);
			this.subsystemManager.removeFromEntityList(entityId);
			this.subsystemManager.removeMessageBank(entityId);
		}
		this.entitiesToDestroy.clear();
	}
	

}
