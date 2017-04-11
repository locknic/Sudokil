package com.custardgames.sudokil.events.commandLine.device;

import com.custardgames.sudokil.utils.Streams;

public class SSHEvent extends DeviceEvent
{
	private String connectingToName;
	
	public SSHEvent(Streams ownerUI, String deviceName, String connectingToName)
	{
		super(ownerUI, deviceName);
		
		this.setConnectingToName(connectingToName);
	}

	public String getConnectingToName()
	{
		return connectingToName;
	}

	public void setConnectingToName(String connectingToName)
	{
		this.connectingToName = connectingToName;
	}
}
