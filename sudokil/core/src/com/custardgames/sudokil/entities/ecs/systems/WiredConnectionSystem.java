package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;
import java.util.UUID;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.ProcessQueueComponent;
import com.custardgames.sudokil.entities.ecs.processes.PhysicalConnectProcess;
import com.custardgames.sudokil.events.entities.ProcessEvent;
import com.custardgames.sudokil.events.entities.commands.WiredConnectEvent;
import com.custardgames.sudokil.managers.EventManager;

public class WiredConnectionSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<EntityComponent> entityComponents;

	@SuppressWarnings("unchecked")
	public WiredConnectionSystem()
	{
		super(Aspect.all(EntityComponent.class, ProcessQueueComponent.class));

		EventManager.get_instance().register(WiredConnectEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(WiredConnectEvent.class, this);
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

	public void connectCommand(UUID consoleUUID, String connectingWith)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			EntityComponent entityComponent = entityComponents.get(entity);

			if (entityComponent.getId().equals(connectingWith))
			{
				PhysicalConnectProcess connectProcess = new PhysicalConnectProcess(consoleUUID, entity);
				EventManager.get_instance().broadcast(new ProcessEvent(entity, connectProcess));
			}
		}
	}

	public void handleWiredConnectEvent(WiredConnectEvent event)
	{
		connectCommand(event.getOwnerUI(), event.getEntityName());
	}

}
