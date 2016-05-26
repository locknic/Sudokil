package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.ActivityBlockingComponent;
import com.custardgames.sudokil.events.entities.BlockActivityEvent;
import com.custardgames.sudokil.events.entities.UnblockActivityEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ActivityBlockingSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<ActivityBlockingComponent> activityBlockingComponents;

	@SuppressWarnings("unchecked")
	public ActivityBlockingSystem()
	{
		super(Aspect.all(ActivityBlockingComponent.class));
		EventManager.get_instance().register(BlockActivityEvent.class, this);
		EventManager.get_instance().register(UnblockActivityEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(BlockActivityEvent.class, this);
		EventManager.get_instance().deregister(UnblockActivityEvent.class, this);
	}

	@Override
	public boolean checkProcessing()
	{
		return false;
	}

	@Override
	protected void process(Entity e)
	{

	}

	public void handleBlockActivity(BlockActivityEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			if (entity == event.getEntity())
			{
				ActivityBlockingComponent activityBlockingComponent = activityBlockingComponents.get(entity);
				activityBlockingComponent.addActivityBlocker(event.getComponentClass());
			}
		}
	}

	public void handleUnblockActivity(UnblockActivityEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			if (entity == event.getEntity())
			{
				ActivityBlockingComponent activityBlockingComponent = activityBlockingComponents.get(entity);
				activityBlockingComponent.removeActivityBlocker(event.getComponentClass());
			}
		}
	}
}
