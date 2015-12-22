package com.custardgames.sudokil.events.commands;

public class StopCommandsEvent extends BaseCommandEvent
{
	public StopCommandsEvent()
	{

	}

	public StopCommandsEvent(String id)
	{
		setOwner(id);
	}
}
