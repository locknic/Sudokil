package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

public class CommandLineEvent extends UserInterfaceEvent
{
	private String text;

	public CommandLineEvent(UUID ownerUI, String text)
	{
		super(ownerUI);
		setText(text);
	}

	public String getText()
	{
		return text;
	}

	public void setText(String command)
	{
		this.text = command;
	}

}
