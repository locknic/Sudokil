package com.custardgames.sudokil.entities.ecs.processes;

import java.util.EventListener;
import java.util.UUID;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.ProcessQueueComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.FileSystemComponent;
import com.custardgames.sudokil.events.DisposeWorldEvent;
import com.custardgames.sudokil.events.PingFileSystemEvent;
import com.custardgames.sudokil.events.commandLine.CloseCommandLineWindowEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleConnectEvent;
import com.custardgames.sudokil.events.entities.commands.DisconnectEvent;
import com.custardgames.sudokil.managers.EventManager;

public class WirelessConnectProcess extends EntityProcess implements EventListener
{
	private UUID consoleUUID;
	private boolean disconnect;

	private Entity connectedTo;
	private boolean connected;

	public WirelessConnectProcess(UUID consoleUUID, Entity entity, Entity connectedTo)
	{
		super(entity);

		EventManager.get_instance().register(DisposeWorldEvent.class, this);
		EventManager.get_instance().register(CloseCommandLineWindowEvent.class, this);

		this.consoleUUID = consoleUUID;
		this.disconnect = false;
		this.connected = false;
		this.connectedTo = connectedTo;
	}

	@Override
	public void dispose()
	{
		EventManager.get_instance().deregister(DisconnectEvent.class, this);
		EventManager.get_instance().deregister(CloseCommandLineWindowEvent.class, this);
		EventManager.get_instance().deregister(DisposeWorldEvent.class, this);
	}

	@Override
	public boolean process()
	{
		if (!connected)
		{
			if (!connect(entity, connectedTo))
			{
				disconnect = true;
			}
			connected = true;
		}
		return disconnect;
	}

	public boolean connect(Entity entity, Entity otherEntity)
	{
		if (otherEntity != null)
		{
			FileSystemComponent fileSystemComponent = otherEntity.getComponent(FileSystemComponent.class);
			if (fileSystemComponent != null)
			{
				PingFileSystemEvent fileSystemEvent = (PingFileSystemEvent) EventManager.get_instance()
						.broadcastInquiry(new PingFileSystemEvent(fileSystemComponent.getFileLocation()));
				EventManager.get_instance().broadcast(new ConsoleConnectEvent(consoleUUID, fileSystemEvent.getFileSystem()));
				EventManager.get_instance().register(DisconnectEvent.class, this);
				return true;
			}
		}
		return false;
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
		String connectedToID = connectedTo.getComponent(EntityComponent.class).getId();
		if (event.getEntityName().equals(entityComponent.getId()) || event.getEntityName().equals(connectedToID))
		{
			disconnect();
		}
	}

	public void handleCloseCommandLineWindow(CloseCommandLineWindowEvent event)
	{
		for (UUID uuid : event.getUuids())
		{
			if (uuid.equals(consoleUUID))
			{
				disconnect();
			}
		}
	}

	public void handleDisposeWorld(DisposeWorldEvent event)
	{
		EventManager.get_instance().deregister(DisposeWorldEvent.class, this);
		EventManager.get_instance().deregister(DisconnectEvent.class, this);
		EventManager.get_instance().deregister(CloseCommandLineWindowEvent.class, this);
	}
}
