package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

public class AutocompleteResponseEvent extends CommandLineEvent
{
	public AutocompleteResponseEvent(UUID ownerUI, String text)
	{
		super(ownerUI, text);
	}
}
