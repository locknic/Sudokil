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
import com.custardgames.sudokil.entities.ecs.components.filesystem.WirelessDeviceComponent;
import com.custardgames.sudokil.entities.ecs.processes.WirelessConnectProcess;
import com.custardgames.sudokil.events.PingEntityEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleLogEvent;
import com.custardgames.sudokil.events.commandLine.device.IfconfigEvent;
import com.custardgames.sudokil.events.commandLine.device.SSHEvent;
import com.custardgames.sudokil.events.entities.ProcessEvent;
import com.custardgames.sudokil.managers.EventManager;

public class NetworkSystem extends EntityProcessingSystem implements EventListener
{
	ComponentMapper<EntityComponent> entityComponents;
	ComponentMapper<WirelessDeviceComponent> wirelessDeviceComponents;
	ComponentMapper<FileSystemComponent> fileSystemComponents;

	@SuppressWarnings("unchecked")
	public NetworkSystem()
	{
		super(Aspect.all(EntityComponent.class, WirelessDeviceComponent.class));
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
				WirelessDeviceComponent otherWirelessDeviceComponent = wirelessDeviceComponents.get(otherEntity);
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
		WirelessDeviceComponent wirelessDeviceComponent = wirelessDeviceComponents.get(entity);

		String output = "";
		for (String connectedNetwork : wirelessDeviceComponent.getWirelessNetworks())
		{
			output += "\nNetwork: " + connectedNetwork + "\n";
			if (networks.containsKey(connectedNetwork))
			{
				for (String value : networks.get(connectedNetwork))
				{
					output += value + "\n";
				}
			}
		}

		EventManager.get_instance().broadcast(new ConsoleLogEvent(connected, output));
	}

	public void ssh(UUID connected, Entity entity, String connectTo)
	{
		HashMap<String, Array<String>> networks = createNetworkHashMap(entity);
		WirelessDeviceComponent wirelessDeviceComponent = wirelessDeviceComponents.get(entity);

		for (String connectedNetwork : wirelessDeviceComponent.getWirelessNetworks())
		{
			if (networks.containsKey(connectedNetwork) && networks.get(connectedNetwork).contains(connectTo, false))
			{
				PingEntityEvent entityEvent = (PingEntityEvent) EventManager.get_instance().broadcastInquiry(new PingEntityEvent(connectTo));
				if (entityEvent != null && entityEvent.getEntity() != null)
				{
					WirelessConnectProcess wirelessConnectProcess = new WirelessConnectProcess(connected, entity, entityEvent.getEntity());
					EventManager.get_instance().broadcast(new ProcessEvent(entity, wirelessConnectProcess));
				}
			}
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
