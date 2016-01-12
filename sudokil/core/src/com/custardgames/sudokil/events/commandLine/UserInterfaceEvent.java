package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

import com.custardgames.sudokil.events.BaseEvent;

public abstract class UserInterfaceEvent extends BaseEvent
{
	private UUID ownerUI;

	public UserInterfaceEvent(UUID ownerUI)
	{
		setOwnerUI(ownerUI);
	}
	
	public UUID getOwnerUI()
	{
		return ownerUI;
	}

	public void setOwnerUI(UUID ownerUI)
	{
		this.ownerUI = ownerUI;
	}
}
