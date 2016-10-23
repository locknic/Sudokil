package com.custardgames.sudokil.events.entities.commands;

public class DisconnectEvent extends EntityCommandEvent
{

	@Override
	public void setDefaultDocumentation()
	{
		setName("disconnect.sh - disconnect robot from device");
		setUsage("disconnect.sh");
		setDescription("Disconnects robots from any other devices that they are physically connected to. \n\nThis script will be run on all robots with a .dev file inside the current directory.");
	}

}
