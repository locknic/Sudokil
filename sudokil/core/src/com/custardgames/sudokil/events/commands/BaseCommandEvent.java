package com.custardgames.sudokil.events.commands;

import java.util.UUID;

import com.custardgames.sudokil.events.BaseEvent;

public class BaseCommandEvent extends BaseEvent
{
	private String[] args;
	private UUID consoleUUID;

	public String[] getArgs()
	{
		return args;
	}

	public void setArgs(String[] args)
	{
		this.args = args;
	}

	public UUID getConsoleUUID()
	{
		return consoleUUID;
	}

	public void setConsoleUUID(UUID consoleUUID)
	{
		this.consoleUUID = consoleUUID;
	}

}
