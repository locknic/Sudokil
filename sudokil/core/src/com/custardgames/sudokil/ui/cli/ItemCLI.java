package com.custardgames.sudokil.ui.cli;

public class ItemCLI
{
	private String name;
	private FolderCLI parent;

	public ItemCLI()
	{
		this("", null);
	}

	public ItemCLI(String name, FolderCLI parent)
	{
		setName(name);
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
	
	public ItemCLI copy()
	{
		ItemCLI newItem = new ItemCLI(name, parent);
		return newItem;
	}

}
