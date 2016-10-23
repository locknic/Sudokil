package com.custardgames.sudokil.events.entities.commands;

public class LowerEvent extends EntityCommandEvent
{

	@Override
	public void setDefaultDocumentation()
	{
		setName("lower.sh - robot lower object");
		setUsage("lower.sh");
		setDescription("Commands robot to lower the object in front of it. \n\nThis script will be run on all robots with a .dev file inside the current directory.");
	}
	
}
