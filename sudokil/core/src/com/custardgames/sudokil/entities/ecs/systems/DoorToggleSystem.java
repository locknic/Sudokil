package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.BlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.DoorGroupComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.entities.ecs.components.current.CurrentConsumerComponent;
import com.custardgames.sudokil.entities.ecs.processes.DoorOffProcess;
import com.custardgames.sudokil.entities.ecs.processes.DoorOnProcess;
import com.custardgames.sudokil.events.PingEntityEvent;
import com.custardgames.sudokil.events.entities.CurrentStorageEvent;
import com.custardgames.sudokil.events.entities.ProcessEvent;
import com.custardgames.sudokil.managers.EventManager;

public class DoorToggleSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<DoorGroupComponent> doorGroupComponents;
	private ComponentMapper<CurrentConsumerComponent> currentConsumerComponents;

	@SuppressWarnings("unchecked")
	public DoorToggleSystem()
	{
		super(Aspect.all(EntityComponent.class, DoorGroupComponent.class, SpriteComponent.class, BlockingComponent.class));

		EventManager.get_instance().register(CurrentStorageEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(CurrentStorageEvent.class, this);
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

	private void turnOffDoor(Entity entity)
	{
		DoorGroupComponent doorGroupComponent = doorGroupComponents.get(entity);

		for (String doorName : doorGroupComponent.getDoorTiles())
		{
			PingEntityEvent event = (PingEntityEvent) EventManager.get_instance().broadcastInquiry(new PingEntityEvent(doorName));

			if (event != null && event instanceof PingEntityEvent)
			{
				Entity doorTile = event.getEntity();
				if (doorTile != null)
				{
					DoorOffProcess doorOffProcess = new DoorOffProcess(doorTile);
					EventManager.get_instance().broadcast(new ProcessEvent(entity, doorOffProcess));
				}
			}
		}
	}

	private void turnOnDoor(Entity entity)
	{
		DoorGroupComponent doorGroupComponent = doorGroupComponents.get(entity);

		for (String doorName : doorGroupComponent.getDoorTiles())
		{
			PingEntityEvent event = (PingEntityEvent) EventManager.get_instance().broadcastInquiry(new PingEntityEvent(doorName));

			if (event != null && event instanceof PingEntityEvent)
			{
				Entity doorTile = event.getEntity();
				if (doorTile != null)
				{
					DoorOnProcess doorOnProcess = new DoorOnProcess(doorTile);
					EventManager.get_instance().broadcast(new ProcessEvent(entity, doorOnProcess));
				}
			}
		}
	}

	public void handleCurrentStorageEvent(CurrentStorageEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			if (event.getEntity().getId() == entity.getId())
			{
				CurrentConsumerComponent currentConsumerComponent = currentConsumerComponents.get(entity);
				if (currentConsumerComponent.isHasCurrent())
				{
					turnOffDoor(entity);
				}
				else
				{
					turnOnDoor(entity);
				}
			}
		}
	}
}
