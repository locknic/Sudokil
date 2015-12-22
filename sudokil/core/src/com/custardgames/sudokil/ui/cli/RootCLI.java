package com.custardgames.sudokil.ui.cli;

import com.badlogic.gdx.utils.Array;

public class RootCLI extends FolderCLI
{
	private Array<ItemCLI> itemList;
	private String deviceName;

	public RootCLI()
	{
		super();
		itemList = new Array<ItemCLI>();
	}

	public ItemCLI getItem(String itemID)
	{
		if (itemID == null || itemID.equals("") || itemID.equals("/") || itemID.equals("~"))
		{
			return this;
		}
		for (ItemCLI e : itemList)
		{
			if (getItemID(e).equals(itemID))
			{
				return e;
			}
		}
		return null;
	}

	public String getItemID(ItemCLI item)
	{
		return item.getLocation() + "/" + item.getName();
	}

	public String getDeviceName()
	{
		return deviceName;
	}
	
	public Array<ItemCLI> getChildren(FolderCLI parent)
	{
		Array<ItemCLI> children = new Array<ItemCLI>();
		for (ItemCLI item : itemList)
		{
			if (item.getLocation() == parent.getLocation())
			{
				children.add(item);
			}
		}
		return children;
	}

	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
	}
}
