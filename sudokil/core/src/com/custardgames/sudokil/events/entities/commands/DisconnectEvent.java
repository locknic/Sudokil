package com.custardgames.sudokil.events.entities.commands;

public class DisconnectEvent extends EntityCommandEvent
{

	@Override
	public void setDefaultUsage()
	{
		setUsage("disconnect.sh");
	}

}
