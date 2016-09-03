package com.custardgames.sudokil.ui.cli;

import java.util.UUID;

import com.custardgames.sudokil.events.BaseEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.events.entities.commands.EntityCommandEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ScriptCLI extends ItemCLI
{
	private BaseEvent event;
	private String usage;

	public ScriptCLI()
	{
		super();
		usage = "";
	}

	public ScriptCLI(String name, FolderCLI parent, BaseEvent event, String usage)
	{
		super(name, parent);
		this.event = event;
		this.usage = usage;
	}

	public void run()
	{
		if (event instanceof EntityCommandEvent)
		{
			for (String device : getParent().getDevices())
			{
				((EntityCommandEvent) event).setEntityName(device);
				EventManager.get_instance().broadcast(event);
			}
		}
		else
		{
			EventManager.get_instance().broadcast(event);
		}
	}

	public void run(UUID ownerUI, String[] args)
	{
		if (args.length > 1 && (args[1].equals("-h") || args[1].equals("--help") || args[1].equals("-help")))
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "usage: " + usage));
		}
		else
		{
			if (event instanceof EntityCommandEvent)
			{
				for (String device : getParent().getDevices())
				{
					((EntityCommandEvent) event).setEntityName(device);
					((EntityCommandEvent) event).setArgs(args);
					((EntityCommandEvent) event).setOwnerUI(ownerUI);
					EventManager.get_instance().broadcast(event);
				}
			}
			else
			{
				EventManager.get_instance().broadcast(event);
			}
		}
	}

	@Override
	public ItemCLI copy()
	{
		return new ScriptCLI(super.getName(), super.getParent(), event, usage);
	}
}
