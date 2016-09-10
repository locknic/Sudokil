package com.custardgames.sudokil.events.commandLine.device;

import java.util.UUID;

import com.custardgames.sudokil.events.commandLine.UserInterfaceEvent;

public abstract class DeviceEvent extends UserInterfaceEvent
{
	private String deviceName;
	
	public DeviceEvent(UUID ownerUI, String deviceName)
	{
		super(ownerUI);
		this.deviceName = deviceName;
	}
	
	public String getDeviceName()
	{
		return deviceName;
	}

}
