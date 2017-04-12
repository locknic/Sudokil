package com.custardgames.sudokil.ui.cli;

import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.utils.Streams;

public class ItemCLI
{
	private String name;
	private FolderCLI parent;
	
	protected boolean readPerm;
	protected boolean writePerm;
	protected boolean executePerm;
	
	public ItemCLI()
	{
		this("", null);
	}

	public ItemCLI(String name, FolderCLI parent)
	{
		setName(name);
		readPerm = false;
		writePerm = false;
		executePerm = true;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public FolderCLI getParent()
	{
		return parent;
	}

	public void setParent(FolderCLI parent)
	{
		this.parent = parent;
	}

	public void changeParent(FolderCLI newParent)
	{
		parent.removeChild(this);
		newParent.addChild(this);
	}

	public String getParentName()
	{
		return parent.getName();
	}

	public String getLocation()
	{
		if (parent != null)
		{
			parent.getPath();
		}
		return "";
	}

	public String getPath()
	{
		if (parent != null)
		{
			if (!parent.getPath().equals(""))
			{
				return parent.getPath() + "/" + getName();
			}
			return getName();
		}
		return "";
	}

	public boolean isReadPerm()
	{
		return readPerm;
	}

	public boolean isWritePerm()
	{
		return writePerm;
	}

	public boolean isExecutePerm()
	{
		return executePerm;
	}
	
	public Object run(Streams ownerUI, String[] args)
	{
		EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Permission denied."));
		
		return null;
	}
	
	public String read()
	{
		return "ERROR! Could not read file.";
	}
	
	public ItemCLI copy()
	{
		ItemCLI newItem = new ItemCLI(name, parent);
		return newItem;
	}

}
