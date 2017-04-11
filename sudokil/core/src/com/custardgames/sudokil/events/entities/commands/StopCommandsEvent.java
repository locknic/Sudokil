package com.custardgames.sudokil.events.entities.commands;

import com.custardgames.sudokil.utils.Streams;

public class StopCommandsEvent extends EntityCommandEvent
{
	public StopCommandsEvent()
	{

	}

	public StopCommandsEvent(Streams ownerUI, String id)
	{
		this.setEntityName(id);
		this.setOwnerUI(ownerUI);
	}
	
	@Override
	public void setDefaultDocumentation()
	{
	}
}
