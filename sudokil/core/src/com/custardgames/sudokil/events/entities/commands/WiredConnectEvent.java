package com.custardgames.sudokil.events.entities.commands;

public class WiredConnectEvent extends EntityCommandEvent
{
	@Override
	public void setDefaultDocumentation()
	{
		setName("connect.sh - connect robot to device");
		setUsage("connect.sh");
		setDescription("Connects robots to a connectable device in front of it. \n\nThis script will be run on all robots with a .dev file inside the current directory.");
	}
}
