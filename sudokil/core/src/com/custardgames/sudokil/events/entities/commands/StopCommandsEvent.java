package com.custardgames.sudokil.events.entities.commands;

public class StopCommandsEvent extends EntityCommandEvent
{
	public StopCommandsEvent()
	{

	}

	public StopCommandsEvent(String id)
	{
		this.setEntityName(id);
	}
}
