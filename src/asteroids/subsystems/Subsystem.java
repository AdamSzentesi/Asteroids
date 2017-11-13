package asteroids.subsystems;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import asteroids.Message;
import asteroids.SubsystemMessenger;
import asteroids.World;
import java.util.HashMap;
import java.util.List;

public class Subsystem
{
	public SubsystemMessenger subsystemMessenger;
	public ArrayList<Message> messages = new ArrayList<>();
	
	public boolean active = true;
	
	public LinkedHashMap<Long, ArrayList<Integer>> iterableEntities = new LinkedHashMap<>();
	private HashMap<String, Long> locksList = new HashMap<>();
	
	public Subsystem(){}
	
	public void setPrimaryLock(long lock)
	{
		System.out.println(", setting 'primary' lock:" + lock);
		this.iterableEntities.put(lock, new ArrayList<>());
		this.locksList.put("primary", lock);
	}
	
	public void addLock(String listName, long lock)
	{
		String subsystemName = this.getClass().getSimpleName();
		System.out.println("SubsystemManager: " + subsystemName + " adding '" + listName + "' lock:" + lock);
		this.iterableEntities.put(lock, new ArrayList<>());
		this.locksList.put(listName, lock);
	}
	
	public ArrayList<Integer> getList(String listName)
	{
		return this.iterableEntities.get(this.locksList.get(listName));
	}
	
	public void setMessenger(SubsystemMessenger subsystemMessenger)
	{
		this.subsystemMessenger = subsystemMessenger;
	}
	
	public void process(World world, float delta)
	{
		long startTime = System.nanoTime();
		preIterate(world, delta);
		iterate(world, delta);
		postIterate(world, delta);
		this.messages.clear();
		long endTime = System.nanoTime();
		float millis = (float)(endTime - startTime)/1000000;
		if(millis > 1)
			System.out.println(this.getClass().getSimpleName() + " time: " + millis);
	}
	
	//NEW
	public void updateEntityList(int entityId, long entityKey)
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
	
	//Iteration sequence
	public void preIterate(World world, float delta){}
	public void iterate(World world, float delta){}
	public void postIterate(World world, float delta){}
	
	//messaging
	public void sendMessage(Message message)
	{
		//System.out.println("Sending MSG: " + message.entityId + " - " + message.parameter);
		this.subsystemMessenger.sendMessage(this, message);
		this.subsystemMessenger.addMessage(message.entityId, message);
	}
	
	public void receiveMessage(Message message)
	{
		//System.out.println("Received: " + message.entityId + " - " + message.parameter);
		this.messages.add(message);
	}
	
	public List<Message> getMessages(int entityId)
	{
		//System.out.println("getting messages for " + entityId);
		return this.subsystemMessenger.getMessages(entityId);
	}
	
	public void cleanUp(){}
	
}
