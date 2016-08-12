package com.custardgames.sudokil.entities.ecs.processes;

import java.util.EventListener;
import java.util.UUID;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.ActivityBlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.ConnectableComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.ProcessQueueComponent;
import com.custardgames.sudokil.events.DisposeWorldEvent;
import com.custardgames.sudokil.events.PingFileSystemEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleConnectEvent;
import com.custardgames.sudokil.events.entities.commands.DisconnectEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ConnectProcess extends EntityProcess implements EventListener
{
	private Entity connectedTo;
	private UUID consoleUUID;
	private boolean disconnect;

	public ConnectProcess(UUID consoleUUID, Entity connectedWith, Entity connectedTo)
	{
		super(connectedWith);

		EventManager.get_instance().register(DisposeWorldEvent.class, this);

		this.consoleUUID = consoleUUID;
		this.connectedTo = connectedTo;
		this.disconnect = false;

		ActivityBlockingComponent activityBlockingComponent = connectedTo.getComponent(ActivityBlockingComponent.class);

		if (activityBlockingComponent == null || (activityBlockingComponent != null && activityBlockingComponent.isActive()))
		{
			ConnectableComponent connectableComponent = connectedTo.getComponent(ConnectableComponent.class);

			if (connectableComponent != null)
			{
				if (connectableComponent.getFileLocation() != null || !connectableComponent.getFileLocation().equals(""))
				{
					PingFileSystemEvent event = (PingFileSystemEvent) EventManager.get_instance()
							.broadcastInquiry(new PingFileSystemEvent(connectableComponent.getFileLocation()));

					if (event.getFileSystem() != null)
					{
						EventManager.get_instance().broadcast(new ConsoleConnectEvent(consoleUUID, event.getFileSystem()));
						EventManager.get_instance().register(DisconnectEvent.class, this);
						return;
					}
				}
			}
		}
		this.disconnect = true;
	}

	@Override
	public void dispose()
	{
		EventManager.get_instance().deregister(DisconnectEvent.class, this);
	}

	@Override
	public boolean process()
	{
		return disconnect;
	}

	public void disconnect()
	{
		EventManager.get_instance().broadcast(new ConsoleConnectEvent(consoleUUID, null));
		ProcessQueueComponent processQueueComponent = entity.getComponent(ProcessQueueComponent.class);
		processQueueComponent.clearQueue();
		disconnect = true;
		EventManager.get_instance().deregister(DisconnectEvent.class, this);
	}

	public void handleDisconnectEvent(DisconnectEvent event)
	{
		EntityComponent entityComponent = entity.getComponent(EntityComponent.class);
		if (event.getEntityName().equals(entityComponent.getId()) || event.getEntityName().equals(connectedTo.getComponent(EntityComponent.class).getId()))
		{
			disconnect();
		}
	}

	public void handleDisposeWorld(DisposeWorldEvent event)
	{
		EventManager.get_instance().deregister(DisposeWorldEvent.class, this);
		EventManager.get_instance().deregister(DisconnectEvent.class, this);
	}
}
