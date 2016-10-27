package com.custardgames.sudokil.entities.ecs.systems.power;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.PowerConsumerComponent;
import com.custardgames.sudokil.entities.ecs.components.lights.ConeLightComponent;
import com.custardgames.sudokil.entities.ecs.components.lights.LightComponent;
import com.custardgames.sudokil.entities.ecs.components.lights.PointLightComponent;
import com.custardgames.sudokil.events.entities.PowerStorageEvent;
import com.custardgames.sudokil.managers.EventManager;

import box2dLight.Light;

public class PowerLightSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<PowerConsumerComponent> powerConsumerComponents;
	private ComponentMapper<PointLightComponent> lightComponents;

	@SuppressWarnings("unchecked")
	public PowerLightSystem()
	{
		super(Aspect.all(PowerConsumerComponent.class).one(PointLightComponent.class, ConeLightComponent.class));

		EventManager.get_instance().register(PowerStorageEvent.class, this);
	}

	@Override
	public void dispose()
	{
		EventManager.get_instance().deregister(PowerStorageEvent.class, this);
	}

	@Override
	public boolean checkProcessing()
	{
		return false;
	}

	@Override
	protected void process(Entity e)
	{
		// TODO Auto-generated method stub

	}

	public void handlePowerStorage(PowerStorageEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			PowerConsumerComponent powerConsumerComponent = powerConsumerComponents.get(entity);
			LightComponent lightComponent = lightComponents.get(entity);

			Light light = lightComponent.getLight();
			if (light != null)
			{
				light.setActive(powerConsumerComponent.isPowered());
			}
		}
	}

}
