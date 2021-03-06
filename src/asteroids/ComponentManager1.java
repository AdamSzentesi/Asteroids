package asteroids;

import asteroids.components.Component;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComponentManager1 extends AbstractComponentManager
{
	private int MAX_COMPONENTS;
	private String[] componentTypes;//remove!!!
	private Class<? extends Component>[] componentStore;
	private int nextComponentTypeId;
	private int nextComponentDataId;
	private Component[] componentData;
	private long[] componentTypeKeys;
	private int[][] entityComponentRelations;
	
	public ComponentManager1(int MAX_COMPONENTS, int MAX_ENTITIES)
	{
		this.MAX_COMPONENTS = MAX_COMPONENTS;
		this.componentTypes = new String[MAX_COMPONENTS];//remove!!!
		this.componentStore = new Class[MAX_COMPONENTS];
		this.nextComponentTypeId = 0;
		this.nextComponentDataId = 1;
		this.componentData = new Component[MAX_ENTITIES * MAX_COMPONENTS];
		this.componentTypeKeys = new long[MAX_COMPONENTS];
		this.entityComponentRelations = new int[MAX_ENTITIES][MAX_COMPONENTS];
	}
	
	@Override
	public long registerComponent(Class componentClass)
	{
		long result = 0;
		String componentName = componentClass.getSimpleName();
		if(!isInArray(componentName, this.componentTypes))
		{
			this.componentTypes[this.nextComponentTypeId] = componentName;
			this.componentStore[this.nextComponentTypeId] = componentClass;
			this.componentTypeKeys[this.nextComponentTypeId] = 1 << this.nextComponentTypeId;
			result = this.componentTypeKeys[this.nextComponentTypeId];
			System.out.println("EntityManager registering " + componentName + " with Id " + this.nextComponentTypeId + " and binary Key " + result);
			this.nextComponentTypeId++;
		}
		else
		{
			int componentId = getComponentId(componentClass);
			System.out.println("ComponentType " + componentName + " already registered as Id " + componentId);
			result = this.getComponentKey(componentId);
		}
		return result;
	}
	
	@Override
	public long addComponent(int entityId, Class componentClass)
	{
		int componentTypeId = getComponentId(componentClass);
		//System.out.println(" EntityManager: adding component " + getComponentType(componentTypeId) + " of component type id " + componentTypeId);
		try
		{
			this.componentData[this.nextComponentDataId] = componentStore[componentTypeId].newInstance();
		}
		catch (InstantiationException ex){Logger.getLogger(EntityManager.class.getName()).log(Level.SEVERE, null, ex);}
		catch (IllegalAccessException ex){Logger.getLogger(EntityManager.class.getName()).log(Level.SEVERE, null, ex);}
		this.entityComponentRelations[entityId][componentTypeId] = this.nextComponentDataId;
		this.nextComponentDataId++;
		return getComponentKey(componentTypeId);
	}
	
	@Override
	public <T extends Component> T getComponent(int entityId, Class<T> c)
	{
		int componentId = this.entityComponentRelations[entityId][getComponentId(c)];
		return c.cast(this.componentData[componentId]);
	}
	
	@Override
	public void destroyComponentsOnEntity(int entityId)
	{
		for(int componentId : this.entityComponentRelations[entityId])
		{
			this.componentData[componentId] = null;
		}
	}
	
	public int getComponentId(Class componentClass)
	{
		int result = 0;
		for(int i = 0; i < this.componentTypes.length; i++)
		{
			if(componentClass.getSimpleName().equals(this.componentTypes[i]))
			{
				result = i;
			}
		}
		return result;
	}
	
	@Override
	public long getComponentKey(Class componentClass)
	{
		int componentTypeId = getComponentId(componentClass);
		long result = this.componentTypeKeys[componentTypeId];
		return result;
	}
	
	public String getComponentType(int id)
	{
		String result = this.componentTypes[id];
		return result;
	}

	private long getComponentKey(int id)
	{
		long result = this.componentTypeKeys[id];
		return result;
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
	<T extends Component> T getComponent(int entityId, long componentKey) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
