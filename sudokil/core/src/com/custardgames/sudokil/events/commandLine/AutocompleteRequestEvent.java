package com.custardgames.sudokil.events.commandLine;

import com.custardgames.sudokil.utils.Streams;

public class AutocompleteRequestEvent extends CommandLineEvent
{
	public AutocompleteRequestEvent(Streams ownerUI, String text)
	{
		super(ownerUI, text);
	}
}
