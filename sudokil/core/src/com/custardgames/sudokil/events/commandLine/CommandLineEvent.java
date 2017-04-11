package com.custardgames.sudokil.events.commandLine;

import com.custardgames.sudokil.utils.Streams;

public class CommandLineEvent extends CommandEvent
{
	public CommandLineEvent(Streams ownerUI, String text)
	{
		super(ownerUI, text);
	}

}
