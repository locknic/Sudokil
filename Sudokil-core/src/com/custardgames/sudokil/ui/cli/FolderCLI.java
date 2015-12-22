package com.custardgames.sudokil.ui.cli;

import java.util.List;

import com.badlogic.gdx.utils.Array;

public class FolderCLI extends ItemCLI
{
	private Array<String> items;

	public FolderCLI()
	{
		super();
		items = new Array<String>();
	}

	public FolderCLI(String name)
	{
		super(name);
		items = new Array<String>();
	}

	public Array<String> getItems()
	{
		return items;
	}

	public void setItems(List<String> items)
	{
		this.items = new Array<String>();

		for (String e : items)
		{
			addItem(e);
		}
	}

	protected void addItem(String item)
	{
		items.add(item);
	}

	protected void removeItem(String itemID)
	{
		items.removeValue(itemID, false);
	}

}
