package com.custardgames.sudokil.events.entities.commands;

import java.util.UUID;

public class StopCommandsEvent extends EntityCommandEvent
{
	public StopCommandsEvent()
	{

	}

	public StopCommandsEvent(UUID ownerUI, String id)
	{
		this.setEntityName(id);
		this.setOwnerUI(ownerUI);
	}
	
	@Override
	public void setDefaultDocumentation()
	{
	}
}
