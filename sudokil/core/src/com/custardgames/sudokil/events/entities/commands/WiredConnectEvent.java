package com.custardgames.sudokil.events.entities.commands;

public class WiredConnectEvent extends EntityCommandEvent
{
	@Override
	public void setDefaultUsage()
	{
		setUsage("connect.sh");
	}
}
