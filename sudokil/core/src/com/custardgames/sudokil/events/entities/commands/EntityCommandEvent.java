package com.custardgames.sudokil.events.entities.commands;

import java.util.UUID;

import com.custardgames.sudokil.events.BaseEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.managers.EventManager;

public abstract class EntityCommandEvent extends BaseEvent
{
	private UUID ownerUI;
	private String entityName;
	
	private String usage;
	
	public EntityCommandEvent()
	{
		usage = "";
		setDefaultUsage();
	}
	
	public String getEntityName()
	{
		return entityName;
	}

	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}

	public boolean setArgs(String[] args)
	{
		return !checkHelpArgs(args);
	}

	public UUID getOwnerUI()
	{
		return ownerUI;
	}

	public void setOwnerUI(UUID ownerUI)
	{
		this.ownerUI = ownerUI;
	}

	public String getUsage()
	{
		return usage;
	}

	public void setUsage(String usage)
	{
		if(usage != null && !usage.equals(""))
		{
			this.usage = usage;
		}
	}
	
	public abstract void setDefaultUsage();

	protected boolean checkHelpArgs(String args[])
	{
		if (args != null && args.length > 1 && (args[1].equals("-h") || args[1].equals("--help") || args[1].equals("-help")))
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(getOwnerUI(), getUsage()));
			return true;
		}
		return false;
	}
	
	protected boolean checkEmptyArgs(String args[])
	{
		if (args == null || args.length <= 1)
		{
			return true;
		}
		return false;
	}
	
	protected boolean checkIntegerArgs(String args[])
	{
		if (args != null && args.length > 1)
		{
			try
			{
				Integer.parseInt(args[1]);
				return true;
			}
			catch (Exception e)
			{
			}
		}
		return false;
	}

}
