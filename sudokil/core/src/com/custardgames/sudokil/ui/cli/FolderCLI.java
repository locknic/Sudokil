package com.custardgames.sudokil.ui.cli;

import java.util.UUID;

import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.managers.EventManager;

public class FolderCLI extends ItemCLI
{
	private Array<ItemCLI> children;

	public FolderCLI()
	{
		this("", null);
	}

	public FolderCLI(String name, FolderCLI parent)
	{
		super(name, parent);
		children = new Array<ItemCLI>();
		readPerm = true;
	}

	public boolean nameTaken(String name)
	{
		for (ItemCLI existingChild : children)
		{
			if (name.equals(existingChild.getName()))
			{
				return true;
			}
		}
		return false;
	}

	public void addChild(ItemCLI child)
	{
		children.add(child);
		child.setParent(this);
	}

	public void removeChild(ItemCLI child)
	{
		children.removeValue(child, true);
		child.setParent(null);
	}

	public Array<ItemCLI> getChildren()
	{
		return children;
	}

	public Array<String> getDevices()
	{
		Array<String> devices = new Array<String>();
		for (ItemCLI child : children)
		{
			if (child.getName().substring(child.getName().length() - 4).equals(".dev"))
			{
				devices.add(child.getName().substring(0, child.getName().length() - 4));
			}
		}
		return devices;
	}
	
	public Array<String> getSubDevices()
	{
		Array<String> devices = new Array<String>();
		for (ItemCLI child : children)
		{
			if (child instanceof FolderCLI)
			{
				devices.addAll(((FolderCLI) child).getAllDevices());
			}
		}
		return devices;
	}
	
	public Array<String> getAllDevices()
	{
		Array<String> devices = new Array<String>();
		devices.addAll(getDevices());
		devices.addAll(getSubDevices());
		return devices;
	}
	
	@Override
	public void run(UUID ownerUI, String[] args)
	{
		EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Is a directory."));
	}

	@Override
	public String read()
	{
		return "ERROR! Is a directory.";
	}
	
	@Override
	public ItemCLI copy()
	{
		FolderCLI newItem = new FolderCLI(super.getName(), super.getParent());
		for (ItemCLI child : children)
		{
			newItem.addChild(child.copy());
		}
		return newItem;
	}
}
