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

	public void addItem(String itemID, ItemCLI item)
	{
		addItemTo("", itemID, item);
	}

	public void addItemTo(String parentID, String itemName, ItemCLI item)
	{
		System.out.println("Moving: " + itemName + " to " + parentID + " from " + item.getParent());
		if (item.getLocation() != null || item.getLocation() != "" || item.getLocation() != "/")
		{
			((FolderCLI)getItem(item.getLocation())).removeItem(getItemID(item));
		}
		if (!parentID.equals("") && !parentID.equals("/") && !parentID.equals("~"))
		{
			ItemCLI parent = getItem(parentID);
			if (parent instanceof FolderCLI)
			{
				item.setParent(parent.getName());
				item.setLocation(getItemID(parent));
				item.setName(itemName);
				((FolderCLI) parent).addItem(getItemID(item));
				itemList.add(item);
			}
		}
		else
		{
			item.setParent("");
			item.setName(itemName);
			item.setLocation("");
			addItem(getItemID(item));
			itemList.add(item);
		}

		if (item instanceof FolderCLI)
		{
			if (((FolderCLI) item).getItems() != null && ((FolderCLI) item).getItems().size > 0)
			{
				for (int x = 0; x < ((FolderCLI) item).getItems().size; x++)
				{
					ItemCLI childItem = getItem(((FolderCLI) item).getItems().get(x));
					deleteItem(getItemID(childItem));
					((FolderCLI) item).removeItem(getItemID(childItem));
					System.out.println("Moving child: " + childItem.getName() + " to " + item.getName());
					addItemTo(getItemID(item), childItem.getName(), childItem);
				}
			}
		}
	}

	public void deleteItem(String itemID)
	{
		ItemCLI item = getItem(itemID);
		((FolderCLI) getItem(item.getLocation())).removeItem(itemID);
		itemList.removeValue(item, false);
	}

	public void moveItem(String parentID, String itemID)
	{
		ItemCLI item = getItem(itemID);
		deleteItem(itemID);
		addItemTo(parentID, item.getName(), item);
	}

	public String getDeviceName()
	{
		return deviceName;
	}

	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
	}
}
