package asteroids;

import asteroids.components.Component;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComponentManager extends AComponentManager
{
	private int MAX_COMPONENTS;
	private int MAX_ENTITIES;
	private int nextComponentTypeId;
	
	private Map<Long, Component[]> components;
	private Map<Long, Class<? extends Component>> componentsStore;
	private Map<String, Long> componentsKeys;
	
	public ComponentManager(int MAX_COMPONENTS, int MAX_ENTITIES)
	{
		this.MAX_COMPONENTS = MAX_COMPONENTS;
		this.MAX_ENTITIES = MAX_ENTITIES;
		this.nextComponentTypeId = 0;
		
		this.components = new HashMap<>();
		this.componentsStore = new HashMap<>();
		this.componentsKeys = new HashMap<>();
	}
	
	@Override
	public long registerComponent(Class componentClass)
	{
		Long result = this.componentsKeys.get(componentClass.getSimpleName());
		
		if(result == null)
		{
			result = (long)1 << this.nextComponentTypeId;
			this.componentsKeys.put(componentClass.getSimpleName(), result);
			this.componentsStore.put(result, componentClass);
			this.components.put(result, (Component[])Array.newInstance(componentClass, this.MAX_ENTITIES));
			System.out.println("EntityManager registering " + componentClass.getSimpleName() + " with Id " + this.nextComponentTypeId + " and binary Key " + result);
			this.nextComponentTypeId++;
		}
		else
		{
			System.out.println("ComponentType " + componentClass.getSimpleName() + " already registered as Id " + result);
		}
		
		return result;
	}
	
	@Override
	public long addComponent(int entityId, Class componentClass)
	{
//		System.out.println(" adding " + componentClass.getSimpleName() + " to entity " + entityId);
		long result = getComponentKey(componentClass);
//		System.out.println(" key " + result);
		addComponent(entityId, result);
		return result;
	}
	
	public long addComponent(int entityId, long componentKey)
	{
		try
		{
			this.components.get(componentKey)[entityId] = componentsStore.get(componentKey).newInstance();
		}
		catch (InstantiationException ex) {Logger.getLogger(ComponentManager.class.getName()).log(Level.SEVERE, null, ex);}
		catch (IllegalAccessException ex) {Logger.getLogger(ComponentManager.class.getName()).log(Level.SEVERE, null, ex);}
		return componentKey;
	}
	
	@Override
	public <T extends Component> T getComponent(int entityId, Class<T> componentClass)
	{
		long componentKey = getComponentKey(componentClass);
		return componentClass.cast(this.components.get(componentKey)[entityId]);
	}

	@Override
	public void destroyComponentsOnEntity(int entityId)
	{
		for(Component[] component : this.components.values())
		{
			component[entityId] = null;
		}
	}
	
	private boolean isInArray(String value, String[] array)
	{
		boolean result = false;
		for(int i = 0; i < array.length; i++)
		{
			if(value.equals(array[i]))
			{
				result = true;
			}
		}
		return result;
	}

	
	@Override
	public long getComponentKey(Class componentClass)
	{
		long result = this.componentsKeys.get(componentClass.getSimpleName());
		return result;
	}

}
