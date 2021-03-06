package com.custardgames.sudokil.ui.cli;

import com.custardgames.sudokil.events.BaseEvent;
import com.custardgames.sudokil.events.entities.commands.EntityCommandEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.utils.Streams;

public class ScriptCLI extends ItemCLI
{
	private BaseEvent event;

	public ScriptCLI()
	{
		this("", null, null);
	}

	public ScriptCLI(String name, FolderCLI parent, BaseEvent event)
	{
		super(name, parent);
		this.event = event;
	}
	
	public BaseEvent getEvent()
	{
		return event;
	}

	@Override
	public void run(Streams ownerUI, String[] args)
	{
		if (event != null && event instanceof EntityCommandEvent)
		{
			((EntityCommandEvent) event).setOwnerUI(ownerUI);
			if (((EntityCommandEvent) event).setArgs(args))
			{
				for (String device : getParent().getDevices())
				{
					((EntityCommandEvent) event).setEntityName(device);
					EventManager.get_instance().broadcast(event);
				}
			}
		}
		else
		{
			EventManager.get_instance().broadcast(event);
		}
	}

	@Override
	public ItemCLI copy()
	{
		return new ScriptCLI(super.getName(), super.getParent(), event);
	}
}
