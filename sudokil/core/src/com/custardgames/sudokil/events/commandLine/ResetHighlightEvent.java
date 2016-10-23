package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

public class ResetHighlightEvent extends UserInterfaceEvent
{

	public ResetHighlightEvent(UUID ownerUI)
	{
		super(ownerUI);
	}

}
