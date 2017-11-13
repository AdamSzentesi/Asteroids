package asteroids;

import java.util.ArrayList;
import java.util.List;
import asteroids.math.Pair;
import asteroids.subsystems.Subsystem;
import java.util.HashMap;
import java.util.Map;

public class SubsystemMessenger
{
	private List<Subsystem> systems = new ArrayList<>();
	private List<Pair<Integer, Integer>> systemRelations = new ArrayList<>();
	private Map<Integer, List<Message>> messages = new HashMap<>();
	
	public void sendMessage(Subsystem subsystem, Message message)
	{
		for(Pair<Integer, Integer> relation : this.systemRelations)
		{
			int senderSystemId = this.systems.indexOf(subsystem);
			//System.out.println("-relation: " + relation.a + " listens to " + relation.b);
			//System.out.println("-sender: " + senderSystemId);
			if(relation.b == senderSystemId)
			{
				//System.out.print("(msg " + subsystem.getClass().getSimpleName() + ">" + this.systems.get(relation.a).getClass().getSimpleName() + ")");
				this.systems.get(relation.a).receiveMessage(message);
			}
		}
	}
	
	public void addMessageBank(int entityId)
	{
		this.messages.put(entityId, new ArrayList<Message>());
	}
	
	public void removeMessageBank(int entityId)
	{
		this.messages.remove(entityId);
	}
	
	public void addMessage(int entityId, Message message)
	{
		this.messages.get(entityId).add(message);
	}
	
	public List<Message> getMessages(int entityId)
	{
		return this.messages.get(entityId);
	}
	
	public void messagesToFrom(Subsystem listener, Subsystem emitter)
	{
		int listenerId = getSubsystemId(listener);
		int emitterId = getSubsystemId(emitter);
		this.systemRelations.add(new Pair(listenerId, emitterId));
		//System.out.println("SubsystemMessenger: " + listener.getClass().getSimpleName() + " listens to " + emitter.getClass().getSimpleName());
	}
	
	private int getSubsystemId(Subsystem subsystem)
	{
		//String className = subsystem.getClass().getSimpleName();
		if(!this.systems.contains(subsystem));
		{
			registerSubsystem(subsystem);
		}
		int result = this.systems.indexOf(subsystem);
		
		return(result);
	}
	
	private void registerSubsystem(Subsystem subsystem)
	{
		this.systems.add(subsystem);
		System.out.println("SubsystemMessenger: registering " + subsystem.getClass().getSimpleName() + " as id " + this.systems.indexOf(subsystem));
	}
	
	public void clearMessages()
	{
		for(List<Message> list : this.messages.values())
		{
			list.clear();
		}
	}
	
}
