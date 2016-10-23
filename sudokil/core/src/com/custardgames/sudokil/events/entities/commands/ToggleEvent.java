package com.custardgames.sudokil.events.entities.commands;

public class ToggleEvent extends EntityCommandEvent
{
	@Override
	public void setDefaultUsage()
	{
		setUsage("toggle.sh");
	}
}
