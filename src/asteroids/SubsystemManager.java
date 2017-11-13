package asteroids;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import asteroids.subsystems.Subsystem;

public class SubsystemManager
{
	private LinkedHashMap<String, Subsystem> subsystems = new LinkedHashMap<>();
	private HashMap<String, Class<? extends Subsystem>> subsystemStore = new HashMap<>();
	private SubsystemMessenger subsystemMessenger = new SubsystemMessenger();
	
	public SubsystemManager(){}
	
	public void process(World world, float delta)
	{
		this.subsystemMessenger.clearMessages();
		for (Subsystem subsystem : this.subsystems.values())
		{
			if(subsystem.active)
			{
				subsystem.process(world, delta);
			}
		}
		
	}
	
	//NEW
	public void updateEntityList(int entityId, long entityKey)
	{
		for (Subsystem subsystem : this.subsystems.values())
		{
			subsystem.updateEntityList(entityId, entityKey);
		}
	}
	
	public void addSubsystem(Class subsystemClass, long componentKey)
	{
		System.out.print("SubsystemManager: adding " + subsystemClass.getSimpleName() + " subsystem");
		try
		{
			register(subsystemClass);
			this.subsystems.put(subsystemClass.getSimpleName(),	this.subsystemStore.get(subsystemClass.getSimpleName()).newInstance());
			
			Subsystem subsystem = this.subsystems.get(subsystemClass.getSimpleName());
			subsystem.setPrimaryLock(componentKey);
			subsystem.setMessenger(this.subsystemMessenger);
		}
		catch (InstantiationException ex){Logger.getLogger(SubsystemManager.class.getName()).log(Level.SEVERE, null, ex);}
		catch (IllegalAccessException ex){Logger.getLogger(SubsystemManager.class.getName()).log(Level.SEVERE, null, ex);}
	}
	
	public <T extends Subsystem> T getSubsystem(Class<T> subsystemClass)
	{
		return subsystemClass.cast(this.subsystems.get(subsystemClass.getSimpleName()));
	}
	
	public void addMessageBank(int entityId)
	{
		this.subsystemMessenger.addMessageBank(entityId);
	}
	
	public void removeMessageBank(int entityId)
	{
		this.subsystemMessenger.removeMessageBank(entityId);
	}
	
	public void messagesToFrom(Class listener, Class emitter)
	{
		this.subsystemMessenger.messagesToFrom(getSubsystem(listener), getSubsystem(emitter));
	}
	
	private void register(Class subsystemClass)
	{
		if(!this.subsystemStore.containsKey(subsystemClass.getSimpleName()))
		{
			this.subsystemStore.put(subsystemClass.getSimpleName(), subsystemClass);
			System.out.print(" - registering as new");
		}
		else
		{
			System.out.print(" - already registered");
		}
	}
	
	public void cleanUp()
	{
		for (Subsystem subsystem : this.subsystems.values())
		{
			subsystem.cleanUp();
		}	
	}
	
}
