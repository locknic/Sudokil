package com.custardgames.sudokil.events.commandLine;

import com.custardgames.sudokil.utils.Streams;

public class AutocompleteResponseEvent extends CommandLineEvent
{
	public AutocompleteResponseEvent(Streams ownerUI, String text)
	{
		super(ownerUI, text);
	}
}
