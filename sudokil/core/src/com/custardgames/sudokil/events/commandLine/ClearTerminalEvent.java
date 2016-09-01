package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

public class ClearTerminalEvent extends UserInterfaceEvent
{
	public ClearTerminalEvent(UUID ownerUI)
	{
		super(ownerUI);
	}
}
