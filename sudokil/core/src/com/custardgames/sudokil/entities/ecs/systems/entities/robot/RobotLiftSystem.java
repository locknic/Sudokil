package com.custardgames.sudokil.entities.ecs.systems.entities.robot;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.LifterComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.processes.LiftProcess;
import com.custardgames.sudokil.entities.ecs.processes.LowerProcess;
import com.custardgames.sudokil.events.entities.EntityMovedEvent;
import com.custardgames.sudokil.events.entities.EntityTurnedEvent;
import com.custardgames.sudokil.events.entities.ProcessEvent;
import com.custardgames.sudokil.events.entities.commands.LiftEvent;
import com.custardgames.sudokil.events.entities.commands.LowerEvent;
import com.custardgames.sudokil.managers.EventManager;

public class RobotLiftSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<EntityComponent> entityComponents;
	private ComponentMapper<LifterComponent> lifterComponents;
	private ComponentMapper<PositionComponent> positionComponents;


	public RobotLiftSystem()
	{
		super(Aspect.all(EntityComponent.class, LifterComponent.class, PositionComponent.class));

		EventManager.get_instance().register(LiftEvent.class, this);
		EventManager.get_instance().register(LowerEvent.class, this);
		EventManager.get_instance().register(EntityTurnedEvent.class, this);
		EventManager.get_instance().register(EntityMovedEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(LiftEvent.class, this);
		EventManager.get_instance().deregister(LowerEvent.class, this);
		EventManager.get_instance().deregister(EntityTurnedEvent.class, this);
		EventManager.get_instance().deregister(EntityMovedEvent.class, this);
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

	public void handleLift(LiftEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			EntityComponent entityComponent = entityComponents.get(entity);
			if (entityComponent.getId().equals(event.getEntityName()))
			{
				LiftProcess liftProcess = new LiftProcess(entity);
				liftProcess.setOutputUUID(event.getOwnerUI());
				EventManager.get_instance().broadcast(new ProcessEvent(entity, liftProcess));
			}
		}
	}

	public void handleLower(LowerEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			EntityComponent entityComponent = entityComponents.get(entity);
			if (entityComponent.getId().equals(event.getEntityName()))
			{
				LowerProcess lowerProcess = new LowerProcess(entity);
				lowerProcess.setOutputUUID(event.getOwnerUI());
				EventManager.get_instance().broadcast(new ProcessEvent(entity, lowerProcess));
			}
		}
	}

	public void handleEntityTurned(EntityTurnedEvent event)
	{
		// ImmutableBag<Entity> entities = getEntities();
		// for (Entity entity : entities)
		// {
		// if (entity == event.getEntity())
		// {
		// LifterComponent lifterComponent = lifterComponents.get(entity);
		// Entity lifted = lifterComponent.getLifted();
		// if (lifted != null)
		// {
		// PositionComponent liftedPositionComponent =
		// positionComponents.get(lifted);
		// if (liftedPositionComponent != null)
		// {
		// liftedPositionComponent.setAngle(liftedPositionComponent.getAngle() +
		// event.getAngle());
		// EventManager.get_instance().broadcast(new EntityTurnedEvent(lifted));
		// }
		// }
		// }
		// }
	}

	public void handleEntityMoved(EntityMovedEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			if (entity == event.getEntity())
			{
				PositionComponent positionComponent = positionComponents.get(entity);
				LifterComponent lifterComponent = lifterComponents.get(entity);
				Entity lifted = lifterComponent.getLifted();
				if (lifted != null)
				{
					PositionComponent liftedPositionComponent = positionComponents.get(lifted);
					if (liftedPositionComponent != null)
					{
						liftedPositionComponent.setPosition(
								positionComponent.getX() + positionComponent.getWidth() / 2 - liftedPositionComponent.getWidth() / 2,
								positionComponent.getY() + positionComponent.getHeight() / 2 - liftedPositionComponent.getHeight() / 2 + 28);
						EventManager.get_instance().broadcast(new EntityMovedEvent(lifted));
					}
				}
			}
		}
	}

}
