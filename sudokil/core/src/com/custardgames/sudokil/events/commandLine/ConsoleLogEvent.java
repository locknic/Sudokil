package com.custardgames.sudokil.events.commandLine;

import com.custardgames.sudokil.utils.Streams;

public class ConsoleLogEvent extends CommandLineEvent
{

	public ConsoleLogEvent(Streams ownerUI, String text)
	{
		super(ownerUI, text);
	}

}
