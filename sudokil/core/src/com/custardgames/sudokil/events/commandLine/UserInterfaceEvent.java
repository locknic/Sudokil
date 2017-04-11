package com.custardgames.sudokil.events.commandLine;

import com.custardgames.sudokil.events.BaseEvent;
import com.custardgames.sudokil.utils.Streams;

public abstract class UserInterfaceEvent extends BaseEvent
{
	private Streams ownerUI;

	public UserInterfaceEvent(Streams ownerUI)
	{
		setOwnerUI(ownerUI);
	}

	public Streams getOwnerUI()
	{
		return ownerUI;
	}

	public void setOwnerUI(Streams ownerUI)
	{
		this.ownerUI = ownerUI;
	}
}
