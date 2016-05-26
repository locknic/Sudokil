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
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(PingEntityEvent.class, this);
	}

	@Override
	public boolean checkProcessing()
	{
		return false;
	}

	@Override
	protected void process(Entity entity)
	{

	}

	public PingEntityEvent handleInquiryPingEntityEvent(PingEntityEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			EntityComponent entityComponent = entityComponents.get(entity);
			String entityID = entityComponent.getId();
			if (event.getEntityID().equals(entityID))
			{
				event.setEntity(entity);
				return event;
			}
		}
		return null;
	}

}
