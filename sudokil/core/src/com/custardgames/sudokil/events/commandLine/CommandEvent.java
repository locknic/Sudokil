package com.custardgames.sudokil.events.commandLine;

import com.custardgames.sudokil.utils.Streams;

public class CommandEvent extends UserInterfaceEvent
{
	private String text;
	
	public CommandEvent(Streams ownerUI, String text)
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
