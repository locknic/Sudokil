package com.custardgames.sudokil.ui.cli;

import java.util.UUID;

import com.custardgames.sudokil.events.BaseEvent;
import com.custardgames.sudokil.events.entities.commands.EntityCommandEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ScriptCLI extends ItemCLI
{
	private BaseEvent event;

	public ScriptCLI()
	{
		super();
	}

	public ScriptCLI(String name, FolderCLI parent, BaseEvent event)
	{
		super(name, parent);
		this.event = event;
	}

	public void run()
	{
		if (event instanceof EntityCommandEvent)
		{
			((EntityCommandEvent) event).setEntityName(getParentName());
		}
		EventManager.get_instance().broadcast(event);
	}

	public void run(UUID ownerUI, String[] args)
	{
		if (event instanceof EntityCommandEvent)
		{
			((EntityCommandEvent) event).setEntityName(getParentName());
			((EntityCommandEvent) event).setArgs(args);
			((EntityCommandEvent) event).setOwnerUI(ownerUI);
		}
		EventManager.get_instance().broadcast(event);
	}

	@Override
	public ItemCLI copy()
	{
		return new ScriptCLI(super.getName(), super.getParent(), event);
	}
}
