package com.custardgames.sudokil.ui.cli;

public class RootCLI extends FolderCLI
{
	private String deviceName;

	public RootCLI()
	{
		super();
	}
	
	public RootCLI(String name, FolderCLI parent, String deviceName)
	{
		super(name, parent);
		this.deviceName = deviceName;
	}

	public String getDeviceName()
	{
		return deviceName;
	}
	
	@Override
	public ItemCLI copy()
	{
		RootCLI newItem = new RootCLI(super.getName(), super.getParent(), deviceName);
		for(ItemCLI child : getChildren())
		{
			newItem.addChild(child.copy());
		}
		return newItem;
	}

}
