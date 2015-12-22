package com.custardgames.sudokil.events;

public class BaseEvent
{
	private String owner;

	public BaseEvent()
	{

	}

	public BaseEvent(String owner)
	{
		this.owner = owner;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}
}
