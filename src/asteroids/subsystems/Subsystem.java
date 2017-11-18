package asteroids.subsystems;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import asteroids.Message;
import asteroids.SubsystemMessenger;
import asteroids.World;
import java.util.HashMap;
import java.util.List;

abstract public class Subsystem
{
	public SubsystemMessenger subsystemMessenger;
	public ArrayList<Message> messages = new ArrayList<>();
	
	public boolean active = true;
	
	public LinkedHashMap<Long, ArrayList<Integer>> iterableEntities = new LinkedHashMap<>();
	private HashMap<String, Long> locksList = new HashMap<>();
	
	public Subsystem(){}
	
	public final void setPrimaryLock(long lock)
	{
		System.out.println(", setting 'primary' lock:" + lock);
		this.iterableEntities.put(lock, new ArrayList<>());
		this.locksList.put("primary", lock);
	}
	
	public final void addLock(String listName, long lock)
	{
		String subsystemName = this.getClass().getSimpleName();
		System.out.println("SubsystemManager: " + subsystemName + " adding '" + listName + "' lock:" + lock);
		this.iterableEntities.put(lock, new ArrayList<>());
		this.locksList.put(listName, lock);
	}
	
	public final ArrayList<Integer> getList(String listName)
	{
		return this.iterableEntities.get(this.locksList.get(listName));
	}
	
	public final void setMessenger(SubsystemMessenger subsystemMessenger)
	{
		this.subsystemMessenger = subsystemMessenger;
	}
	
	public final void upate(World world, float delta)
	{
		long startTime = System.nanoTime();

		process(world, delta);
		this.messages.clear();

		long endTime = System.nanoTime();
		float millis = (float)(endTime - startTime)/1000000;
//		if(millis > 1)
//			System.out.println(this.getClass().getSimpleName() + " time: " + millis);
	}
	
	//Iteration sequence
	abstract public void process(World world, float delta);
	
	final ArrayList<Integer> getPrimaryList()
	{
		return this.iterableEntities.get(this.locksList.get("primary"));
	}
	
	public final void updateEntityList(int entityId, long entityKey)
	{
		//update iterable entitites
		for(Map.Entry<Long, ArrayList<Integer>> entry : this.iterableEntities.entrySet())
		{
			long lock = entry.getKey();
			ArrayList<Integer> entities = entry.getValue();
			
			if((entityKey & lock) == lock)
			{
				if(!entities.contains(entityId))
				{
					entities.add(entityId);
				}
			}
			else
			{
				if(entities.contains(entityId))
				{
					int id = entities.indexOf(entityId);
					entities.remove(id);
				}
			}
		}

	}
	
	//messaging
	public final void sendMessage(Message message)
	{
		//System.out.println("Sending MSG: " + message.entityId + " - " + message.parameter);
		this.subsystemMessenger.sendMessage(this, message);
		this.subsystemMessenger.addMessage(message.entityId, message);
	}
	
	public final void receiveMessage(Message message)
	{
		//System.out.println("Received: " + message.entityId + " - " + message.parameter);
		this.messages.add(message);
	}
	
	public final List<Message> getMessages(int entityId)
	{
		//System.out.println("getting messages for " + entityId);
		return this.subsystemMessenger.getMessages(entityId);
	}
	
	public void cleanUp(){}

	public void removeFromEntityList(int entityId)
	{
		for(ArrayList<Integer> entities : this.iterableEntities.values())
		{
			if(entities.contains(entityId))
			{
				int position = entities.indexOf(entityId);
				entities.remove(position);
			}
		}
	}
	
}
