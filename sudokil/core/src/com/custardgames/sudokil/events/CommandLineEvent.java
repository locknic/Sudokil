package com.custardgames.sudokil.events;

import java.util.UUID;

public class CommandLineEvent extends BaseEvent
{
	private String command;
	private UUID ownerUI;

	public CommandLineEvent(String command, UUID owner)
	{
		setCommand(command);
		setOwnerUI(owner);
	}

	public String getCommand()
	{
		return command;
	}

	public void setCommand(String command)
	{
		this.command = command;
	}

	public UUID getOwnerUI()
	{
		return ownerUI;
	}

	public void setOwnerUI(UUID ownerUI)
	{
		this.ownerUI = ownerUI;
	}

}
