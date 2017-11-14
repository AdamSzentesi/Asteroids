package asteroids;

import asteroids.components.Component;

public abstract class aComponentManager
{
	/**
	 * Registers the component type in the ComponentManager
	 * 
	 * @param componentClass component class to register
	 * @return 
	 */
	abstract long registerComponent(Class componentClass);
	
	abstract long addComponent(int entityId, Class componentClass);
	
	abstract <T extends Component> T getComponent(int entityId, Class<T> componentClass);
	
	abstract void destroyComponentOnEntity(int entityId);
	
	abstract int getComponentId(Class componentClass);
	
	abstract long getComponentKey(Class componentClass);
	
	abstract String getComponentType(int id);
}
