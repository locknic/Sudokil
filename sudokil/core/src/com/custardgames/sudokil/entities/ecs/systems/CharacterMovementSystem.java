package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.ProcessQueueComponent;
import com.custardgames.sudokil.entities.ecs.components.VelocityComponent;
import com.custardgames.sudokil.entities.ecs.processes.MoveProcess;
import com.custardgames.sudokil.entities.ecs.processes.TurnProcess;
import com.custardgames.sudokil.events.entities.ProcessEvent;
import com.custardgames.sudokil.events.entities.commands.BackwardEvent;
import com.custardgames.sudokil.events.entities.commands.ForwardEvent;
import com.custardgames.sudokil.events.entities.commands.LeftEvent;
import com.custardgames.sudokil.events.entities.commands.RightEvent;
import com.custardgames.sudokil.managers.EventManager;

public class CharacterMovementSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<EntityComponent> entityComponents;

	@SuppressWarnings("unchecked")
	public CharacterMovementSystem()
	{
		super(Aspect.all(EntityComponent.class, ProcessQueueComponent.class, PositionComponent.class, VelocityComponent.class));

		EventManager.get_instance().register(ForwardEvent.class, this);
		EventManager.get_instance().register(BackwardEvent.class, this);
		EventManager.get_instance().register(LeftEvent.class, this);
		EventManager.get_instance().register(RightEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(ForwardEvent.class, this);
		EventManager.get_instance().deregister(BackwardEvent.class, this);
		EventManager.get_instance().deregister(LeftEvent.class, this);
		EventManager.get_instance().deregister(RightEvent.class, this);
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

	public void moveCommand(String owner, int distance, int direction)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			EntityComponent entityComponent = entityComponents.get(entity);

			if (entityComponent.getId().equals(owner))
			{
				for (int y = 0; y < distance; y++)
				{
					MoveProcess moveProcess = new MoveProcess(entity, direction);
					EventManager.get_instance().broadcast(new ProcessEvent(entity, moveProcess));
				}
			}
		}
	}

	public void turnCommand(String owner, int times, float angle)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			EntityComponent entityComponent = entityComponents.get(entity);

			if (entityComponent.getId().equals(owner))
			{
				for (int y = 0; y < times; y++)
				{
					TurnProcess turnProcess = new TurnProcess(entity, 90);
					EventManager.get_instance().broadcast(new ProcessEvent(entity, turnProcess));
				}
			}
		}
	}

	public void handleForward(ForwardEvent event)
	{
		moveCommand(event.getEntityName(), event.getDistance(), 1);
	}

	public void handleBackward(BackwardEvent event)
	{
		moveCommand(event.getEntityName(), event.getDistance(), -1);
	}

	public void handleLeft(LeftEvent event)
	{
		turnCommand(event.getEntityName(), event.getNum(), 90);
	}

	public void handleRight(RightEvent event)
	{
		turnCommand(event.getEntityName(), event.getNum(), -90);
	}

}
