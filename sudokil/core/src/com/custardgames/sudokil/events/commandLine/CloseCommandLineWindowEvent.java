package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

public class CloseCommandLineWindowEvent extends UserInterfaceEvent
{

	public CloseCommandLineWindowEvent(UUID ownerUI)
	{
		super(ownerUI);
	}
}
