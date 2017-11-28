# Asteroids
Asteroids game clone: a demo of ECS game engine development in Java.

**The engine is still in development**

demo: [https://www.youtube.com/watch?v=m4q8qE4vyrc](https://www.youtube.com/watch?v=m4q8qE4vyrc)

## Entity-Componen-System
is based on data oriented design where game objects are not based on class inheritance and have no methods. Game objects are identified as Entities and have properties defined by Components. This type of composition lets user to define game objects with different behavior very quickly, parts of the engine can be easily turned on and off and decoupled.

### Entities
are just integers, have no class. Entities reside in EntityManager

### Components
are different subclasses of Component superclass, have only data, no methods (exept for special setters etc.), most of the data is public. Components reside in ComponentManager.

### Systems (I used 'Subsystems' because 'System' is a Java class)
are logic of the engine, are called one after another in cycles, act like processors that input, change or output data. Subsystems reside in SubsystemManager. Subsystems comunicate via SubsystemMessenger

## World
is where EntityManager, ComponentManager and SubsystemManager resides. World executes logic of creating and destroying of game objects.

### Ease of use
```
//GAME INIT
//create a world. This has max. 100 game objects
World world = new World(100);

//register components and get unique keys
long transform2DComponentKey = world.registerComponent(Transform2DComponent.class);
long rigidbody2DComponentKey = world.registerComponent(Rigidbody2DComponent.class);

//add subsystems, use the keys for primary components of interest
world.addSubsystem(Physics2DMoveSubsystem.class, transform2DComponentKey | rigidbody2DComponentKey);

//create game objects: this object will be automatically processed by Physics2DMoveSubsystem and will act by its logic as long as the update loop loops
int entityId = world.createEntity();
world.addComponent(entityId, Transform2DComponent.class);
  world.getComponent(entityId, Transform2DComponent.class).transform.position.set(5f, 4f);
  world.getComponent(entityId, Transform2DComponent.class).transform.scale.set(2.0f, 2.0f);
world.addComponent(entityId, Rigidbody2DComponent.class);
  world.getComponent(entityId, Rigidbody2DComponent.class).mass = 3000.0f;
  world.getComponent(entityId, Rigidbody2DComponent.class).maxVelocity = 5.0f;
  world.getComponent(entityId, Rigidbody2DComponent.class).velocity.set(0.0f, 1.0f);
 
//UPDATE CYCLE
this.world.update(delta);

//RENDER CYCLE
this.world.render(0);

//CLEANUP AT THE END
this.world.cleanUp();
```
