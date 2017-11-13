package asteroids.factory;

import asteroids.EntityManager;
import asteroids.components.Component;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewFactory
{
	public NewFactory()
	{}
	
	public void summonEntity(EntityManager entityManager, String entityType)
	{
		
	}
	
	public <T extends Component> T produceComponent(Class<T> componentClass)
	{
		try
		{
			return componentClass.newInstance();
		}
		catch (InstantiationException ex){Logger.getLogger(EntityManager.class.getName()).log(Level.SEVERE, null, ex);}
		catch (IllegalAccessException ex){Logger.getLogger(EntityManager.class.getName()).log(Level.SEVERE, null, ex);}
		return null;
	}
}
