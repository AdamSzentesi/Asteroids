# Asteroids
Asteroids game clone: a demo of ECS game engine development in Java

## Entity-Componen-System
is based on data oriented design where game objects are not based on class inheritance and have no methods. Game objects are identified as Entities and have properties defined by Components. This type of composition let's user to define game objects with different behavior very quickly, parts of the engine can be easily turned on and off.

### Entities
are just integers, have no class. Entities reside in EntityManager

### Components
are different subclasses of Coomponent superclass, have only data, no methods (exept for special setters), most of the data is public. Components reside in ComponentManager.

### Systems (Subsystems for System is a java class)
are logic of the engine, are called one after another in cycles, act like processors that input, change or output data. Subsystems reside in SubsystemManager. Subsystems comunicate via SubsystemMessenger
