package com.custardgames.sudokil.ui.cli;

public class ItemCLI
{
	private String name;
	private String location;
	private String parent;

	public ItemCLI()
	{
		setName("");
		setLocation("");
		setParent("");
	}

	public ItemCLI(String name)
	{
		setName("");
		setLocation("");

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

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getParent()
	{
		return parent;
	}

	public void setParent(String parent)
	{
		this.parent = parent;
	}

}
