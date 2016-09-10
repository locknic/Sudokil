package com.custardgames.sudokil.entities.ecs.components.filesystem;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class WirelessNetworksComponent extends Component
{
	private Array<String> networkNames;
	
	public WirelessNetworksComponent()
	{
		setNetworkNames(new Array<String>());
	}

	public Array<String> getNetworkNames()
	{
		return networkNames;
	}

	public void setNetworkNames(Array<String> networkNames)
	{
		this.networkNames = networkNames;
	}

}
