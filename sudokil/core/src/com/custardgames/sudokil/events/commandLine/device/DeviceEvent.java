package com.custardgames.sudokil.events.commandLine.device;

import com.custardgames.sudokil.events.commandLine.UserInterfaceEvent;
import com.custardgames.sudokil.utils.Streams;

public abstract class DeviceEvent extends UserInterfaceEvent
{
	private String deviceName;
	
	public DeviceEvent(Streams ownerUI, String deviceName)
	{
		super(ownerUI);
		this.deviceName = deviceName;
	}
	
	public String getDeviceName()
	{
		return deviceName;
	}

}
