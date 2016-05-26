package com.custardgames.sudokil.ui.cli;

import com.badlogic.gdx.utils.Array;

public class FolderCLI extends ItemCLI
{
	private Array<ItemCLI> children;

	public FolderCLI()
	{
		super("", null);
	}

	public FolderCLI(String name, FolderCLI parent)
	{
		super(name, parent);
		children = new Array<ItemCLI>();
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

}
