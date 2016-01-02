package com.custardgames.sudokil.entities.ecs.systems;

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
import com.custardgames.sudokil.events.ProcessEvent;
import com.custardgames.sudokil.events.commands.LiftEvent;
import com.custardgames.sudokil.events.entities.EntityMovedEvent;
import com.custardgames.sudokil.managers.EventManager;

public class LiftSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<EntityComponent> entityComponents;
	private ComponentMapper<LifterComponent> lifterComponents;
	private ComponentMapper<PositionComponent> positionComponents;

	@SuppressWarnings("unchecked")
	public LiftSystem()
	{
		super(Aspect.all(EntityComponent.class, LifterComponent.class, PositionComponent.class));
		EventManager.get_instance().register(LiftEvent.class, this);
		EventManager.get_instance().register(EntityMovedEvent.class, this);
	}

	@Override
	protected void process(Entity e)
	{

	}

	public void handleLift(LiftEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity e : entities)
		{
			EntityComponent entityComponent = entityComponents.get(e);
			if (entityComponent.getId().equals(event.getOwner()))
			{
				LiftProcess liftProcess = new LiftProcess(e);
				EventManager.get_instance().broadcast(new ProcessEvent(entityComponent.getId(), liftProcess));
			}
		}
	}

	public void handleEntityMoved(EntityMovedEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity e : entities)
		{
			EntityComponent entityComponent = entityComponents.get(e);
			if (entityComponent.getId().equals(event.getOwner()))
			{
				LifterComponent lifterComponent = lifterComponents.get(e);
				Entity lifted = lifterComponent.getLifted();
				if (lifted != null)
				{
					PositionComponent positionComponent = positionComponents.get(e);
					PositionComponent liftedPositionComponent = lifted.getComponent(PositionComponent.class);
					if (liftedPositionComponent != null)
					{
						liftedPositionComponent.setPosition(positionComponent.getX(), positionComponent.getY());
					}
				}
			}
		}
	}

}
