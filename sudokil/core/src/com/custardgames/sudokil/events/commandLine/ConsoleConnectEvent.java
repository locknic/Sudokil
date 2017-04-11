package com.custardgames.sudokil.events.commandLine;

import com.custardgames.sudokil.ui.cli.RootCLI;
import com.custardgames.sudokil.utils.Streams;

public class ConsoleConnectEvent extends UserInterfaceEvent
{
	private RootCLI newRoot;

	public ConsoleConnectEvent(Streams ownerUI, RootCLI newRoot)
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
