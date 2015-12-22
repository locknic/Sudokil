package com.custardgames.sudokil.ui.cli;

import java.util.UUID;

import com.custardgames.sudokil.events.commands.BaseCommandEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ScriptCLI extends ItemCLI
{
	private BaseCommandEvent event;

	public void run()
	{
		event.setOwner(getParent());
		EventManager.get_instance().broadcast(event);
	}

	public void run(UUID consoleUUID, String[] args)
	{
		System.out.println(getParent());
		event.setOwner(getParent());
		event.setArgs(args);
		event.setConsoleUUID(consoleUUID);
		EventManager.get_instance().broadcast(event);
	}
}
