package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.NetworkConnectionComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.WirelessDeviceComponent;
import com.custardgames.sudokil.entities.ecs.components.filesystem.WirelessNetworksComponent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.events.entities.map.PingCellEvent;
import com.custardgames.sudokil.events.entities.map.RemoveFromMapEvent;
import com.custardgames.sudokil.managers.EventManager;

public class NetworksConnectedSystem extends EntityProcessingSystem implements EventListener
{
	ComponentMapper<WirelessDeviceComponent> wirelessDeviceComponents;
	ComponentMapper<WirelessNetworksComponent> wirelessNetworkComponents;
	ComponentMapper<NetworkConnectionComponent> networkConnectionComponents;

	@SuppressWarnings("unchecked")
	public NetworksConnectedSystem()
	{
		super(Aspect.all(WirelessDeviceComponent.class));
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
			WirelessDeviceComponent wirelessDeviceComponent = wirelessDeviceComponents.get(entity);
			wirelessDeviceComponent.clearWirelessNetwork();

			WirelessNetworksComponent wirelessNetworkComponent = wirelessNetworkComponents.get(entity);
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
					System.out.println("Looking at " + connectedEntity.getComponent(EntityComponent.class).getId() + " connected to " + entity.getComponent(EntityComponent.class).getId());
					WirelessNetworksComponent connectedWirelessNetworkComponent = wirelessNetworkComponents.get(connectedEntity);
					if (connectedWirelessNetworkComponent != null)
					{
						for (String network : connectedWirelessNetworkComponent.getNetworkNames())
						{
							wirelessDeviceComponent.addWirelessNetwork(network);
						}
					}
				}
			}
		}
	}

	public Array<Entity> findConnectedEntities(Entity entity, Array<Entity> searchedEntities)
	{
		System.out.println("Found entity " + entity.getComponent(EntityComponent.class).getId());

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
