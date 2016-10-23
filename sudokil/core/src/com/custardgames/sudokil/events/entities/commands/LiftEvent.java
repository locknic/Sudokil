package com.custardgames.sudokil.events.entities.commands;

public class LiftEvent extends EntityCommandEvent
{

	@Override
	public void setDefaultUsage()
	{
		setUsage("lift.sh");
	}
	
}
