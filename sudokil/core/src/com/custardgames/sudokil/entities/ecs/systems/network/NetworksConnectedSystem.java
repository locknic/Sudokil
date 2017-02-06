package com.custardgames.sudokil.entities.ecs.systems.network;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.entities.ecs.components.ActivityBlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.NetworkConnectionComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.NetworkedDeviceComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.WirelessRouterComponent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.events.entities.map.PingCellEvent;
import com.custardgames.sudokil.events.entities.map.RemoveFromMapEvent;
import com.custardgames.sudokil.managers.EventManager;

public class NetworksConnectedSystem extends EntityProcessingSystem implements EventListener
{
	ComponentMapper<NetworkedDeviceComponent> wirelessDeviceComponents;
	ComponentMapper<WirelessRouterComponent> wirelessNetworkComponents;
	ComponentMapper<NetworkConnectionComponent> networkConnectionComponents;
	ComponentMapper<EntityComponent> entityComponents;
	ComponentMapper<ActivityBlockingComponent> blockingComponents;


	public NetworksConnectedSystem()
	{
		super(Aspect.all(NetworkedDeviceComponent.class));
		EventManager.get_instance().register(AddToMapEvent.class, this);
		EventManager.get_instance().register(RemoveFromMapEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(AddToMapEvent.class, this);
		EventManager.get_instance().deregister(RemoveFromMapEvent.class, this);
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

	public void checkConnections()
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			NetworkedDeviceComponent wirelessDeviceComponent = wirelessDeviceComponents.get(entity);
			wirelessDeviceComponent.clearWirelessNetwork();
			wirelessDeviceComponent.clearWiredDevices();

			WirelessRouterComponent wirelessNetworkComponent = wirelessNetworkComponents.get(entity);
			if (wirelessNetworkComponent != null)
			{
				wirelessDeviceComponent.setWirelessNetworks(wirelessNetworkComponent.getNetworkNames());
			}

			NetworkConnectionComponent networkConnectionComponent = networkConnectionComponents.get(entity);
			if (networkConnectionComponent != null)
			{
				Array<Entity> connectedEntities = findConnectedEntities(entity, new Array<Entity>());
				for (Entity connectedEntity : connectedEntities)
				{
					if (connectedEntity != entity)
					{
						NetworkedDeviceComponent connectedWirelessDeviceComponent = wirelessDeviceComponents.get(connectedEntity);
						EntityComponent entityComponent = entityComponents.get(connectedEntity);
						ActivityBlockingComponent blockingComponent = blockingComponents.get(entity);
						ActivityBlockingComponent connectedBlockingComponent = blockingComponents.get(connectedEntity);
						
						if (blockingComponent == null || blockingComponent.isActive())
						{
							if (connectedBlockingComponent == null || connectedBlockingComponent.isActive())
							{
								if (connectedWirelessDeviceComponent != null && entityComponent != null)
								{
									wirelessDeviceComponent.addWiredDevice(entityComponent.getId());
								}

								WirelessRouterComponent connectedWirelessRouterComponent = wirelessNetworkComponents.get(connectedEntity);
								if (connectedWirelessRouterComponent != null)
								{
									for (String network : connectedWirelessRouterComponent.getNetworkNames())
									{
										wirelessDeviceComponent.addWirelessNetwork(network);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public Array<Entity> findConnectedEntities(Entity entity, Array<Entity> searchedEntities)
	{
		NetworkConnectionComponent networkConnectionComponent = networkConnectionComponents.get(entity);
		if (networkConnectionComponent != null)
		{
			Entity inputEntity = null;
			if (networkConnectionComponent.isLeft())
			{
				inputEntity = ((PingCellEvent) EventManager.get_instance().broadcastInquiry(new PingCellEvent(entity, -1, 0))).getCellEntity();
				if (inputEntity != null && !searchedEntities.contains(inputEntity, true))
				{
					searchedEntities.add(inputEntity);
					searchedEntities = addNew(searchedEntities, findConnectedEntities(inputEntity, searchedEntities));
				}
			}
			if (networkConnectionComponent.isRight())
			{
				inputEntity = ((PingCellEvent) EventManager.get_instance().broadcastInquiry(new PingCellEvent(entity, 1, 0))).getCellEntity();
				if (inputEntity != null && !searchedEntities.contains(inputEntity, true))
				{
					searchedEntities.add(inputEntity);
					searchedEntities = addNew(searchedEntities, findConnectedEntities(inputEntity, searchedEntities));
				}
			}
			if (networkConnectionComponent.isUp())
			{
				inputEntity = ((PingCellEvent) EventManager.get_instance().broadcastInquiry(new PingCellEvent(entity, 0, 1))).getCellEntity();
				if (inputEntity != null && !searchedEntities.contains(inputEntity, true))
				{
					searchedEntities.add(inputEntity);
					searchedEntities = addNew(searchedEntities, findConnectedEntities(inputEntity, searchedEntities));
				}
			}
			if (networkConnectionComponent.isDown())
			{
				inputEntity = ((PingCellEvent) EventManager.get_instance().broadcastInquiry(new PingCellEvent(entity, 0, -1))).getCellEntity();
				if (inputEntity != null && !searchedEntities.contains(inputEntity, true))
				{
					searchedEntities.add(inputEntity);
					searchedEntities = addNew(searchedEntities, findConnectedEntities(inputEntity, searchedEntities));
				}
			}
		}
		return searchedEntities;
	}

	public Array<Entity> addNew(Array<Entity> arr1, Array<Entity> arr2)
	{
		for (Entity entity : arr2)
		{
			if (!arr1.contains(entity, true))
			{
				arr1.add(entity);
			}
		}
		return arr1;
	}

	public void handleAddToMap(AddToMapEvent event)
	{
		checkConnections();
	}

	public void handleRemoveFromMap(RemoveFromMapEvent event)
	{
		checkConnections();
	}

}
