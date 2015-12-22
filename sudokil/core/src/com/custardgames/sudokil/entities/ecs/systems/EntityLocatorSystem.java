package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.events.PingEntityEvent;
import com.custardgames.sudokil.managers.EventManager;

public class EntityLocatorSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<EntityComponent> entityComponents;

	@SuppressWarnings("unchecked")
	public EntityLocatorSystem()
	{
		super(Aspect.all(EntityComponent.class));
		EventManager.get_instance().register(PingEntityEvent.class, this);
	}

	@Override
	protected void process(Entity arg0)
	{
		// TODO Auto-generated method stub

	}

	public PingEntityEvent handleInquiryPingEntityEvent(PingEntityEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			EntityComponent entityComponent = entityComponents.get(entities.get(x));
			String entityID = entityComponent.getId();
			if (event.getEntityID().equals(entityID))
			{
				event.setEntity(entities.get(x));
				return event;
			}
		}
		return null;
	}

}
