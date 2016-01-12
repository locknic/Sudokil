package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

public class AutocompleteRequestEvent extends CommandLineEvent
{
	public AutocompleteRequestEvent(UUID ownerUI, String text)
	{
		super(ownerUI, text);
	}
}
