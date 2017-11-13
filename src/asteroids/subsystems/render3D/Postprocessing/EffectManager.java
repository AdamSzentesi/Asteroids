package asteroids.subsystems.render3D.Postprocessing;

import java.util.HashMap;

public class EffectManager
{
	public HashMap<String, Effect> effects;
	
	public EffectManager()
	{
		this.effects = new HashMap<>();
	}
	
	public void addEffect(String effectName, Effect effect)
	{
		this.effects.put(effectName, effect);
	}
	
	public <T extends Effect> T getEffect(String effectName, Class<T> effectClass)
	{
		return effectClass.cast(this.effects.get(effectName));
	}
	
	public void cleanUp()
	{
		for (Effect effect : this.effects.values())
		{
			effect.cleanUp();
		}
	}
}
