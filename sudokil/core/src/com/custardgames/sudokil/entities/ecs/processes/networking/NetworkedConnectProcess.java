package com.custardgames.sudokil.entities.ecs.processes.networking;

import java.util.EventListener;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.FileSystemComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.NetworkedDeviceComponent;
import com.custardgames.sudokil.events.PingFileSystemEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleConnectEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.utils.Streams;

public class NetworkedConnectProcess extends ConnectProcess implements EventListener
{
	private NetworkedDeviceComponent networkedDeviceComponent;
	private EntityComponent connectedEntityComponent;
	private NetworkedDeviceComponent connectedNetworkedDeviceComponent;

	public NetworkedConnectProcess(Streams consoleUUID, Entity entity, Entity connectedTo)
	{
		super(consoleUUID, entity, connectedTo);
		
		networkedDeviceComponent = entity.getComponent(NetworkedDeviceComponent.class);
		connectedEntityComponent = connectedTo.getComponent(EntityComponent.class);
		connectedNetworkedDeviceComponent = connectedTo.getComponent(NetworkedDeviceComponent.class);
		
		this.setBackgroundProcess(true);
	}

	@Override
	public boolean preProcess()
	{
		boolean canContinue = super.preProcess();
		
		return canContinue && networkedDeviceComponent != null && connectedEntityComponent != null && connectedNetworkedDeviceComponent != null;
	}

	@Override
	public boolean process()
	{
		super.process();

		if (!disconnect)
		{
			boolean shouldDisconnect = true;

			if (networkedDeviceComponent.getWiredDevices().contains(connectedEntityComponent.getId(), false))
			{
				shouldDisconnect = false;
			}
			for (String network : networkedDeviceComponent.getWirelessNetworks())
			{
				if (connectedNetworkedDeviceComponent.getWirelessNetworks().contains(network, false))
				{
					shouldDisconnect = false;
				}
			}

			if (shouldDisconnect)
			{
				hardDisconnect();
			}
		}

		return disconnect;
	}

	@Override
	public void postProcess()
	{
		// TODO Auto-generated method stub

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
