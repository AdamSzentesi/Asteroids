package asteroids.messenger;

public class Msg extends AbstractMsg
{
	
}
/*
template <class MessageType>class MessageListener
{
	public: virtual void HandleMessage(MessageType* message) = 0;
};

//SYSTEM
class MovementSystem : public System, public MessageListener, public MessageListener
{
public:
	MovementSystem(MessageManager* msgManager);
	virtual void Update();
	virtual ~MovementSystem(void);
	void HandleMessage(MovementMessage* message);
	void HandleMessage(RotationMessage* message);
	virtual void RegisterMessages();
};

void MovementSystem::RegisterMessages()
{
	mMessageManager->RegisterListener(this);
	mMessageManager->RegisterListener(this);
}

int _tmain(int argc, _TCHAR* argv[])
{
	MessageManager messManager;
	EntityManager manager(&messManager);
	Entity* newEnt = manager.CreateEntity( string( "MyFirstEntity" ) );
	newEnt->AddComponent( newEnt );
	newEnt->AddComponent( newEnt );
	newEnt->AddSystem();
	messManager.SendMessage( 40.0f, 0.2f, 0.0f );
	messManager.SendMessage( 20.0f, 0.0f, 0.0f );
	return 0;
}
*/