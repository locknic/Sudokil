package com.custardgames.sudokil.entities.ecs.components.filesystem;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class WirelessDeviceComponent extends Component
{
	private Array<String> wirelessNetworks;
	
	public WirelessDeviceComponent()
	{
		wirelessNetworks = new Array<String>();
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
	
}
