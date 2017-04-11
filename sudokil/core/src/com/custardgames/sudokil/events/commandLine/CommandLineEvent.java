package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

public class CommandLineEvent extends CommandEvent
{
	public CommandLineEvent(UUID ownerUI, String text)
	{
		super(ownerUI, text);
	}

}
