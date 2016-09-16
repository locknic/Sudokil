package com.custardgames.sudokil.events.commandLine.device;

import java.util.UUID;

public class SSHEvent extends DeviceEvent
{
	private String connectingToName;
	
	public SSHEvent(UUID ownerUI, String deviceName, String connectingToName)
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
