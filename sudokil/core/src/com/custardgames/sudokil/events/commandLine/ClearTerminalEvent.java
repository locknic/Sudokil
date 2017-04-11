package com.custardgames.sudokil.events.commandLine;

import com.custardgames.sudokil.utils.Streams;

public class ClearTerminalEvent extends UserInterfaceEvent
{
	public ClearTerminalEvent(Streams ownerUI)
	{
		super(ownerUI);
	}
}
