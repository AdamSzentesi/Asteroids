package asteroids;

import asteroids.components.Component;

/**
 * AbstractComponentManager
 * 
 * @author Adam Szentesi
 */
public abstract class AbstractComponentManager
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
	abstract <T extends Component> T getComponent(int entityId, long componentKey);
	
	abstract void destroyComponentsOnEntity(int entityId);
	
	abstract long getComponentKey(Class componentClass);
}