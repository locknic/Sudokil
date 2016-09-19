package com.custardgames.sudokil.entities.ecs.processes.networking;

import java.util.EventListener;
import java.util.UUID;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.FileSystemComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.NetworkedDeviceComponent;
import com.custardgames.sudokil.events.PingFileSystemEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleConnectEvent;
import com.custardgames.sudokil.managers.EventManager;

public class NetworkedConnectProcess extends ConnectProcess implements EventListener
{

	public NetworkedConnectProcess(UUID consoleUUID, Entity entity, Entity connectedTo)
	{
		super(consoleUUID, entity, connectedTo);

		this.setBackgroundProcess(true);
	}

	@Override
	public boolean process()
	{
		boolean prev = super.process();

		if (!prev && triedConnecting)
		{
			NetworkedDeviceComponent networkedDeviceComponent = entity.getComponent(NetworkedDeviceComponent.class);
			EntityComponent connectedEntityComponent = connectedTo.getComponent(EntityComponent.class);
			NetworkedDeviceComponent connectedNetworkedDeviceComponent = connectedTo.getComponent(NetworkedDeviceComponent.class);

			if (networkedDeviceComponent != null)
			{
				if (connectedEntityComponent != null)
				{
					if (networkedDeviceComponent.getWiredDevices().contains(connectedEntityComponent.getId(), false))
					{
						return false;
					}
				}
				if (connectedNetworkedDeviceComponent != null)
				{
					for (String network : networkedDeviceComponent.getWirelessNetworks())
					{
						if (connectedNetworkedDeviceComponent.getWirelessNetworks().contains(network, false))
						{
							return false;
						}
					}
				}
			}
			disconnect();
		}

		return disconnect;
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
