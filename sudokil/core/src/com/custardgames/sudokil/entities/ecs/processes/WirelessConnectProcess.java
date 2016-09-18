package com.custardgames.sudokil.entities.ecs.processes;

import java.util.EventListener;
import java.util.UUID;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.filesystem.FileSystemComponent;
import com.custardgames.sudokil.events.PingFileSystemEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleConnectEvent;
import com.custardgames.sudokil.managers.EventManager;

public class WirelessConnectProcess extends ConnectProcess implements EventListener
{

	public WirelessConnectProcess(UUID consoleUUID, Entity entity, Entity connectedTo)
	{
		super(consoleUUID, entity, connectedTo);
		
		this.setBackgroundProcess(true);
	}

	@Override
	public boolean connect()
	{
		if (connectedTo != null)
		{
			FileSystemComponent fileSystemComponent = connectedTo.getComponent(FileSystemComponent.class);
			if (fileSystemComponent != null)
			{
				PingFileSystemEvent fileSystemEvent = (PingFileSystemEvent) EventManager.get_instance()
						.broadcastInquiry(new PingFileSystemEvent(fileSystemComponent.getFileLocation()));
				EventManager.get_instance().broadcast(new ConsoleConnectEvent(consoleUUID, fileSystemEvent.getFileSystem()));
				return true;
			}
		}
		return false;
	}
}
