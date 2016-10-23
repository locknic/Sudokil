package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;
import java.util.HashMap;
import java.util.UUID;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.FileSystemComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.NetworkedDeviceComponent;
import com.custardgames.sudokil.entities.ecs.processes.networking.NetworkedConnectProcess;
import com.custardgames.sudokil.events.PingEntityEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleLogEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.events.commandLine.device.IfconfigEvent;
import com.custardgames.sudokil.events.commandLine.device.SSHEvent;
import com.custardgames.sudokil.events.entities.ProcessEvent;
import com.custardgames.sudokil.managers.EventManager;

public class NetworkSystem extends EntityProcessingSystem implements EventListener
{
	ComponentMapper<EntityComponent> entityComponents;
	ComponentMapper<NetworkedDeviceComponent> wirelessDeviceComponents;
	ComponentMapper<FileSystemComponent> fileSystemComponents;

	@SuppressWarnings("unchecked")
	public NetworkSystem()
	{
		super(Aspect.all(EntityComponent.class, NetworkedDeviceComponent.class));
		EventManager.get_instance().register(IfconfigEvent.class, this);
		EventManager.get_instance().register(SSHEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(IfconfigEvent.class, this);
		EventManager.get_instance().deregister(SSHEvent.class, this);
	}

	@Override
	protected void process(Entity e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkProcessing()
	{
		return false;
	}

	public HashMap<String, Array<String>> createNetworkHashMap(Entity entity)
	{
		HashMap<String, Array<String>> networks = new HashMap<String, Array<String>>();

		ImmutableBag<Entity> entities = getEntities();
		for (Entity otherEntity : entities)
		{
			if (otherEntity != entity)
			{
				NetworkedDeviceComponent otherWirelessDeviceComponent = wirelessDeviceComponents.get(otherEntity);
				EntityComponent otherEntityComponent = entityComponents.get(otherEntity);

				for (String networkName : otherWirelessDeviceComponent.getWirelessNetworks())
				{
					if (!networks.containsKey(networkName))
					{
						networks.put(networkName, new Array<String>());
					}

					networks.get(networkName).add(otherEntityComponent.getId());
				}
			}
		}

		return networks;
	}

	public void ifconfig(UUID connected, Entity entity)
	{
		HashMap<String, Array<String>> networks = createNetworkHashMap(entity);
		NetworkedDeviceComponent wirelessDeviceComponent = wirelessDeviceComponents.get(entity);

		String output = "";
		if (wirelessDeviceComponent.getWiredDevices().size > 0)
		{
			output += "Ethernet: " + "\n";
			for (String connectedDevice : wirelessDeviceComponent.getWiredDevices())
			{
				output += "\t" + connectedDevice + "\n";
			}
		}
		for (String connectedNetwork : wirelessDeviceComponent.getWirelessNetworks())
		{
			output += "Network: " + connectedNetwork + "\n";
			if (networks.containsKey(connectedNetwork))
			{
				for (String value : networks.get(connectedNetwork))
				{
					output += "\t" + value + "\n";
				}
			}
		}
		
		if (output.length() > 0)
		{
			EventManager.get_instance().broadcast(new ConsoleLogEvent(connected, output));
		}
	}

	public void ssh(UUID connected, Entity entity, String connectTo)
	{
		HashMap<String, Array<String>> networks = createNetworkHashMap(entity);
		NetworkedDeviceComponent networkedDeviceComponent = wirelessDeviceComponents.get(entity);

		boolean foundWired = false;
		boolean foundWireless = false;
		
		if (networkedDeviceComponent.getWiredDevices().contains(connectTo, false))
		{
			foundWired = true;
			connectTo(connected, entity, connectTo);
		}
		else
		{
			for (String connectedNetwork : networkedDeviceComponent.getWirelessNetworks())
			{
				if (networks.containsKey(connectedNetwork) && networks.get(connectedNetwork).contains(connectTo, false))
				{
					foundWireless = true;
					connectTo(connected, entity, connectTo);
				}
			}
		}
		
		if (!foundWired && !foundWireless)
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(connected, "ERROR! Connect to host " + connectTo + ": operation timed out."));
		}
	}

	public void connectTo(UUID connected, Entity entity, String connectTo)
	{
		PingEntityEvent entityEvent = (PingEntityEvent) EventManager.get_instance().broadcastInquiry(new PingEntityEvent(connectTo));
		if (entityEvent != null && entityEvent.getEntity() != null)
		{
			NetworkedConnectProcess networkedConnectProcess = new NetworkedConnectProcess(connected, entity, entityEvent.getEntity());
			EventManager.get_instance().broadcast(new ProcessEvent(entity, networkedConnectProcess));
		}
	}

	public void handleIfconfig(IfconfigEvent event)
	{
		PingEntityEvent entityEvent = (PingEntityEvent) EventManager.get_instance().broadcastInquiry(new PingEntityEvent(event.getDeviceName()));
		if (entityEvent != null && entityEvent.getEntity() != null)
		{
			ifconfig(event.getOwnerUI(), entityEvent.getEntity());
		}
	}

	public void handleSSH(SSHEvent event)
	{
		PingEntityEvent entityEvent = (PingEntityEvent) EventManager.get_instance().broadcastInquiry(new PingEntityEvent(event.getDeviceName()));
		if (entityEvent != null && entityEvent.getEntity() != null)
		{
			ssh(event.getOwnerUI(), entityEvent.getEntity(), event.getConnectingToName());
		}
	}

}
