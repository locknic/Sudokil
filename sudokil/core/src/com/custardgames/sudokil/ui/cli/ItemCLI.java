package com.custardgames.sudokil.ui.cli;

public class ItemCLI
{
	private String name;
	private String location;

	public ItemCLI()
	{
		this("");
	}

	public ItemCLI(String name)
	{
		setName(name);
		setLocation("");
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}
	
	public String getParentName()
	{
		String[] locationStructure = location.split("/");
		return locationStructure[locationStructure.length - 1];
	}

}
