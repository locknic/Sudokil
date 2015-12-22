package com.custardgames.sudokil.entities.ecs.processes;

import java.util.EventListener;
import java.util.UUID;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.ConnectableComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.ProcessQueueComponent;
import com.custardgames.sudokil.events.ConsoleConnectEvent;
import com.custardgames.sudokil.events.PingFileSystemEvent;
import com.custardgames.sudokil.events.commands.DisconnectEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ConnectProcess extends EntityProcess implements EventListener
{
	private Entity connectedTo;
	private UUID consoleUUID;
	private boolean disconnect;

	public ConnectProcess(UUID consoleUUID, Entity connectedWith, Entity connectedTo)
	{
		super(connectedWith);
		this.consoleUUID = consoleUUID;
		this.connectedTo = connectedTo;
		disconnect = false;

		ConnectableComponent connectableComponent = connectedTo.getComponent(ConnectableComponent.class);

		if (connectableComponent != null)
		{

			if (connectableComponent.getFileLocation() != null || !connectableComponent.getFileLocation().equals(""))
			{
				PingFileSystemEvent event = (PingFileSystemEvent) EventManager.get_instance()
						.broadcastInquiry(new PingFileSystemEvent(connectableComponent.getFileLocation()));

				event.getFileSystem();
				if (event.getFileSystem() != null)
				{
					EventManager.get_instance().broadcast(new ConsoleConnectEvent(consoleUUID, event.getFileSystem()));
				}
				else
				{
					disconnect();
				}
			}
			else
			{
				disconnect();
			}
		}
		else
		{
			disconnect();
		}

		EventManager.get_instance().register(DisconnectEvent.class, this);
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
	}

	public void handleDisconnectEvent(DisconnectEvent event)
	{
		EntityComponent entityComponent = entity.getComponent(EntityComponent.class);
		System.out.println(event.getOwner() + " is either: " + entityComponent.getId() + ", or "
				+ connectedTo.getComponent(EntityComponent.class).getId());
		if (event.getOwner().equals(entityComponent.getId())
				|| event.getOwner().equals(connectedTo.getComponent(EntityComponent.class).getId()))
		{
			disconnect();
		}
	}

}
