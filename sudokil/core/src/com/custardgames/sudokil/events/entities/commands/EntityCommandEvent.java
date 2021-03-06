package com.custardgames.sudokil.events.entities.commands;

import com.custardgames.sudokil.events.BaseEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.utils.Streams;

public abstract class EntityCommandEvent extends BaseEvent
{
	private Streams ownerUI;
	private String entityName;
	
	private String name;
	private String usage;
	private String description;
	
	public EntityCommandEvent()
	{
		usage = "";
		name = "";
		description = "";
		setDefaultDocumentation();
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

	public Streams getOwnerUI()
	{
		return ownerUI;
	}

	public void setOwnerUI(Streams ownerUI)
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
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public abstract void setDefaultDocumentation();

	protected boolean checkHelpArgs(String args[])
	{
		if (args != null && args.length > 1 && (args[1].equals("-h") || args[1].equals("--help") || args[1].equals("-help")))
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(getOwnerUI(), "Usage: " + getUsage() + "\n" + getDescription()));
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
