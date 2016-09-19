package com.custardgames.sudokil.entities.ecs.components.filesystem;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class NetworkedDeviceComponent extends Component
{
	private Array<String> wirelessNetworks;
	private Array<String> wiredDevices;
	
	public NetworkedDeviceComponent()
	{
		wirelessNetworks = new Array<String>();
		wiredDevices = new Array<String>();
	}
	
	public Array<String> getWirelessNetworks()
	{
		return wirelessNetworks;
	}
	
	public void setWirelessNetworks(Array<String> wirelessTransmitNetworks)
	{
		this.wirelessNetworks = wirelessTransmitNetworks;
	}
	
	public void addWirelessNetwork(String networkName)
	{
		if (!wirelessNetworks.contains(networkName, false))
		{
			wirelessNetworks.add(networkName);
		}
	}
	
	public void clearWirelessNetwork()
	{
		wirelessNetworks.clear();
	}

	public Array<String> getWiredDevices()
	{
		return wiredDevices;
	}

	public void setWiredDevices(Array<String> wiredDevices)
	{
		this.wiredDevices = wiredDevices;
	}
	
	public void addWiredDevice(String wiredDevice)
	{
		if (!wiredDevices.contains(wiredDevice, false))
		{
			this.wiredDevices.add(wiredDevice);
		}
	}
	
	public void clearWiredDevices()
	{
		wiredDevices.clear();
	}
	
}
