package com.custardgames.sudokil.triggers.conditions;

import java.util.EventListener;

import com.custardgames.sudokil.events.DisposeWorldEvent;
import com.custardgames.sudokil.events.commandLine.ChangedDirectoryEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ChangeDirectoryCondition extends BaseTriggerCondition implements EventListener
{
	private boolean triggered;

	private String triggerDirectory;

	public ChangeDirectoryCondition()
	{
		EventManager.get_instance().register(DisposeWorldEvent.class, this);
		EventManager.get_instance().register(ChangedDirectoryEvent.class, this);
		triggered = false;
	}

	@Override
	public boolean checkConditions()
	{
		return triggered;
	}

	public void handleChangedDirectory(ChangedDirectoryEvent event)
	{
		if (isRunning())
		{
			if (event.getDirectory().equals(triggerDirectory))
			{
				triggered = true;
			}
		}
	}

	public void handleDisposeWorld(DisposeWorldEvent event)
	{
		EventManager.get_instance().deregister(DisposeWorldEvent.class, this);
		EventManager.get_instance().deregister(ChangedDirectoryEvent.class, this);
	}

}
