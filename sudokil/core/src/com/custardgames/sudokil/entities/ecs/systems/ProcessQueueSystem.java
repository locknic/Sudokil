package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.ProcessQueueComponent;
import com.custardgames.sudokil.events.ProcessEvent;
import com.custardgames.sudokil.events.commands.StopCommandsEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ProcessQueueSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<ProcessQueueComponent> processQueueComponents;
	private ComponentMapper<EntityComponent> entityComponents;

	@SuppressWarnings("unchecked")
	public ProcessQueueSystem()
	{
		super(Aspect.all(ProcessQueueComponent.class));
		EventManager.get_instance().register(ProcessEvent.class, this);
		EventManager.get_instance().register(StopCommandsEvent.class, this);
	}

	@Override
	protected void process(Entity e)
	{
		processQueueComponents.get(e).process();
	}

	public void handleProcess(ProcessEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			EntityComponent entityComponent = entityComponents.get(entities.get(x));
			ProcessQueueComponent processQueueComponent = processQueueComponents.get(entities.get(x));
			if (entityComponent.getId().equals(event.getOwner()))
			{
				processQueueComponent.addToQueue(event.getProcess());
			}
		}
	}

	public void stopCommand(String owner)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			EntityComponent entityComponent = entityComponents.get(entities.get(x));
			ProcessQueueComponent processQueueComponent = processQueueComponents.get(entities.get(x));

			if (owner == null || owner.equals("") || entityComponent.getId().equals(owner))
			{
				processQueueComponent.clearQueue();
			}
		}
	}

	public void handleStopCommandsEvent(StopCommandsEvent event)
	{
		stopCommand(event.getOwner());
	}

}
