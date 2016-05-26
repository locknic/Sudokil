package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.ActivityBlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.ProcessQueueComponent;
import com.custardgames.sudokil.events.entities.ProcessEvent;
import com.custardgames.sudokil.events.entities.commands.StopCommandsEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ProcessQueueSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<ProcessQueueComponent> processQueueComponents;
	private ComponentMapper<EntityComponent> entityComponents;
	private ComponentMapper<ActivityBlockingComponent> activityBlockingComponents;

	@SuppressWarnings("unchecked")
	public ProcessQueueSystem()
	{
		super(Aspect.all(EntityComponent.class, ProcessQueueComponent.class));

		EventManager.get_instance().register(ProcessEvent.class, this);
		EventManager.get_instance().register(StopCommandsEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(ProcessEvent.class, this);
		EventManager.get_instance().deregister(StopCommandsEvent.class, this);
	}

	@Override
	protected void process(Entity entity)
	{
		processQueueComponents.get(entity).process();
	}

	public void handleProcess(ProcessEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			ProcessQueueComponent processQueueComponent = processQueueComponents.get(entity);
			if (entity == event.getEntity())
			{
				ActivityBlockingComponent activityBlockingComponent = activityBlockingComponents.get(entity);
				if (activityBlockingComponent != null)
				{
					if (activityBlockingComponent.isActive())
					{
						processQueueComponent.addToQueue(event.getProcess());
					}
				}
				else
				{
					processQueueComponent.addToQueue(event.getProcess());
				}
			}
		}
	}

	public void stopCommand(String owner)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			EntityComponent entityComponent = entityComponents.get(entity);
			ProcessQueueComponent processQueueComponent = processQueueComponents.get(entity);

			if (owner == null || owner.equals("") || entityComponent.getId().equals(owner))
			{
				processQueueComponent.clearQueue();
			}
		}
	}

	public void handleStopCommands(StopCommandsEvent event)
	{
		stopCommand(event.getEntityName());
	}

}
