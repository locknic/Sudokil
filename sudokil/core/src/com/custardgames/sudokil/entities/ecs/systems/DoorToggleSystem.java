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
import com.custardgames.sudokil.entities.ecs.components.ProcessQueueComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.entities.ecs.components.current.CurrentConsumerComponent;
import com.custardgames.sudokil.entities.ecs.processes.DoorOffProcess;
import com.custardgames.sudokil.entities.ecs.processes.DoorOnProcess;
import com.custardgames.sudokil.entities.ecs.processes.EntityProcessQueue;
import com.custardgames.sudokil.events.PingEntityEvent;
import com.custardgames.sudokil.events.entities.CurrentStorageEvent;
import com.custardgames.sudokil.events.entities.ProcessEvent;
import com.custardgames.sudokil.events.entities.map.PingCellEvent;
import com.custardgames.sudokil.managers.EventManager;

public class DoorToggleSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<DoorGroupComponent> doorGroupComponents;
	private ComponentMapper<CurrentConsumerComponent> currentConsumerComponents;
	private ComponentMapper<ProcessQueueComponent> processQueueComponents;

	@SuppressWarnings("unchecked")
	public DoorToggleSystem()
	{
		super(Aspect.all(EntityComponent.class, DoorGroupComponent.class, SpriteComponent.class, BlockingComponent.class, ProcessQueueComponent.class));

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
		ProcessQueueComponent processQueueComponent = processQueueComponents.get(entity);
		processQueueComponent.clearQueue();
		EventManager.get_instance().broadcast(new ProcessEvent(entity, getDoorOffProcesses(entity)));
	}

	private void turnOnDoor(Entity entity)
	{
		ProcessQueueComponent processQueueComponent = entity.getComponent(ProcessQueueComponent.class);
		processQueueComponent.clearQueue();
		EventManager.get_instance().broadcast(new ProcessEvent(entity, getDoorOnProcesses(entity)));
	}
	
	private EntityProcessQueue getDoorOffProcesses(Entity entity)
	{
		DoorGroupComponent doorGroupComponent = doorGroupComponents.get(entity);
		EntityProcessQueue entityProcessQueue = new EntityProcessQueue(entity);
		
		doorGroupComponent.getDoorTiles().reverse();
		for (String doorName : doorGroupComponent.getDoorTiles())
		{
			PingEntityEvent event = (PingEntityEvent) EventManager.get_instance().broadcastInquiry(new PingEntityEvent(doorName));

			if (event != null && event instanceof PingEntityEvent)
			{
				Entity doorTile = event.getEntity();
				if (doorTile != null)
				{
					DoorOffProcess doorOffProcess = new DoorOffProcess(doorTile);
					entityProcessQueue.addToQueue(doorOffProcess);
				}
			}
		}
		doorGroupComponent.getDoorTiles().reverse();
		return entityProcessQueue;
	}
	
	private EntityProcessQueue getDoorOnProcesses(Entity entity)
	{
		DoorGroupComponent doorGroupComponent = doorGroupComponents.get(entity);
		EntityProcessQueue entityProcessQueue = new EntityProcessQueue(entity);
		boolean blocked = false;
		for (String doorName : doorGroupComponent.getDoorTiles())
		{
			PingEntityEvent event = (PingEntityEvent) EventManager.get_instance().broadcastInquiry(new PingEntityEvent(doorName));

			if (event != null && event instanceof PingEntityEvent)
			{
				Entity doorTile = event.getEntity();
				if (doorTile != null)
				{
					PingCellEvent cellEvent = (PingCellEvent) EventManager.get_instance().broadcastInquiry(new PingCellEvent(doorTile, 0, 0));
					if (cellEvent!= null && cellEvent.getCellEntity() != null && cellEvent.getCellEntity() != doorTile)
					{
						blocked = true; 
						break;
					}
					
					DoorOnProcess doorOnProcess = new DoorOnProcess(doorTile);
					entityProcessQueue.addToQueue(doorOnProcess);
				}
			}
		}
		
		if (blocked)
		{
			entityProcessQueue.addAllToQueue(getDoorOffProcesses(entity).getQueue());
		}
		return entityProcessQueue;
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
