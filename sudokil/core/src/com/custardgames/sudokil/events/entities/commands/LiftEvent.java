package com.custardgames.sudokil.events.entities.commands;

public class LiftEvent extends EntityCommandEvent
{

	@Override
	public void setDefaultDocumentation()
	{
		setName("lift.sh - robot lift object");
		setUsage("lift.sh");
		setDescription("Commands robot to lift the object in front of it. \n\nThis script will be run on all robots with a .dev file inside the current directory.");
	}
	
}
