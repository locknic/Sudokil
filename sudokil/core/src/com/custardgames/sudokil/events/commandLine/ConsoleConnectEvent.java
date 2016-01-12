package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

import com.custardgames.sudokil.ui.cli.RootCLI;

public class ConsoleConnectEvent extends UserInterfaceEvent
{
	private RootCLI newRoot;

	public ConsoleConnectEvent(UUID ownerUI, RootCLI newRoot)
	{
		super(ownerUI);
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
}
