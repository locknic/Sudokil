package com.custardgames.sudokil.entities.ecs.processes.networking;

import java.util.EventListener;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.ActivityBlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.ConnectableComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.FileSystemComponent;
import com.custardgames.sudokil.events.PingFileSystemEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleConnectEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.events.entities.map.PingCellEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.utils.Streams;

public class PhysicalConnectProcess extends ConnectProcess implements EventListener
{

	public PhysicalConnectProcess(Streams consoleUUID, Entity entity)
	{
		super(consoleUUID, entity, null);
	}

	@Override
	public boolean connect()
	{
		PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
		
		PingCellEvent pingCell = ((PingCellEvent) EventManager.get_instance().broadcastInquiry(new PingCellEvent(entity,
				(int) Math.cos(Math.toRadians(positionComponent.getAngle())), (int) Math.sin(Math.toRadians(positionComponent.getAngle())))));
		if (pingCell != null)
		{
			connectedTo = pingCell.getCellEntity();
			if (connectedTo != null)
			{
				ActivityBlockingComponent activityBlockingComponent = connectedTo.getComponent(ActivityBlockingComponent.class);
				if (activityBlockingComponent == null || (activityBlockingComponent != null && activityBlockingComponent.isActive()))
				{
					FileSystemComponent fileSystemComponent = connectedTo.getComponent(FileSystemComponent.class);
					if (connectedTo.getComponent(ConnectableComponent.class) != null && fileSystemComponent != null)
					{
						if (fileSystemComponent.getFileLocation() != null || !fileSystemComponent.getFileLocation().equals(""))
						{
							PingFileSystemEvent event = (PingFileSystemEvent) EventManager.get_instance()
									.broadcastInquiry(new PingFileSystemEvent(fileSystemComponent.getFileLocation()));

							if (event.getFileSystem() != null)
							{
								EventManager.get_instance().broadcast(new ConsoleConnectEvent(consoleUUID, event.getFileSystem()));
								return true;
							}
						}
					}
				}
			}
		}
		
		EventManager.get_instance().broadcast(new ConsoleOutputEvent(consoleUUID, "ERROR! No device found."));
		
		return false;
	}

	@Override
	public void postProcess()
	{
		// TODO Auto-generated method stub
		
	}
}
