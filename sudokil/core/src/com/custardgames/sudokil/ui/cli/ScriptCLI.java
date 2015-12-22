package com.custardgames.sudokil.ui.cli;

import java.util.UUID;

import com.custardgames.sudokil.events.commands.BaseCommandEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ScriptCLI extends ItemCLI
{
	private BaseCommandEvent event;

	public void run()
	{
		event.setOwner(getParentName());
		EventManager.get_instance().broadcast(event);
	}

	public void run(UUID consoleUUID, String[] args)
	{
		event.setOwner(getParentName());
		event.setArgs(args);
		event.setConsoleUUID(consoleUUID);
		EventManager.get_instance().broadcast(event);
	}
}
