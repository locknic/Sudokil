package com.custardgames.sudokil.events;

import java.util.UUID;

import com.custardgames.sudokil.ui.cli.RootCLI;

public class ConsoleConnectEvent extends BaseEvent
{
	private UUID consoleUUID;
	private RootCLI newRoot;

	public ConsoleConnectEvent(UUID consoleUUID, RootCLI newRoot)
	{
		setConsoleUUID(consoleUUID);
		setNewRoot(newRoot);
	}

	public RootCLI getNewRoot()
	{
		return newRoot;
	}

	public void setNewRoot(RootCLI newRoot)
	{
		this.newRoot = newRoot;
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
