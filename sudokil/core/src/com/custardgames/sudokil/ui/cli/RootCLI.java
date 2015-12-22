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
		if (item != this && !item.getLocation().equals(""))
		{
			return item.getLocation() + "/" + item.getName();
		}
		else if (item.getLocation().equals(""))
		{
			return item.getName();
		}
		return "";
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
			System.out.println("Checking item " + item.getName() + " on " + getItemID(parent));
			if (item.getLocation().equals(getItemID(parent)))
			{
				children.add(item);
			}
		}
		return children;
	}
	
	public ItemCLI findItem(ItemCLI currentItem, String location)
	{
		ItemCLI newItem = null;
		boolean foundLocation = false;
		
		if (location != null && (location.equals("") || location.equals("~") || location.equals("/")))
		{
			newItem = this;
			return newItem;
		}
		
		if (location != null && (location.substring(0, 1).equals("/") || location.substring(0, 1).equals("~")))
		{
			newItem = this;
			foundLocation = true;
		}
		else if (currentItem != null)
		{
			newItem = currentItem;
		}		
		String[] locations;
		locations = location.split("/");
		for (int x = 0; x < locations.length; x++)
		{
			if (locations[x].length() > 0)
			{
				foundLocation = false;

				if (locations[x].equals(".."))
				{
					if (!newItem.getLocation().equals(""))
					{
						newItem = getItem(newItem.getLocation());
						foundLocation = true;
					}
					else
					{
						newItem = this;
						foundLocation = true;
					}
				}
				else if (newItem instanceof FolderCLI)
				{
					for (ItemCLI child : getChildren(((FolderCLI) newItem)))
					{
						if (child instanceof FolderCLI)
						{
							if (locations[x].equals(child.getName()))
							{
								newItem = (FolderCLI) child;
								foundLocation = true;
								break;
							}
						}
					}
				}

				if (foundLocation == false)
				{
					newItem = null;
					break;
				}
			}
		}
		return newItem;
	}


	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
	}
}
