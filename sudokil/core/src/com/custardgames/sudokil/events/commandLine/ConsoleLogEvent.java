package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

public class ConsoleLogEvent extends CommandLineEvent
{

	public ConsoleLogEvent(UUID ownerUI, String text)
	{
		super(ownerUI, text);
	}

}
