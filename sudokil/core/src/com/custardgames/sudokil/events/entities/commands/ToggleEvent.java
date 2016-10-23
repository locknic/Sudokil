package com.custardgames.sudokil.events.entities.commands;

public class ToggleEvent extends EntityCommandEvent
{
	@Override
	public void setDefaultDocumentation()
	{
		setName("toggle.sh - toggle output from device");
		setUsage("toggle.sh");
		setDescription("Toggle the state of current output from device. If device is not outputting a current, it will start. If device is outputting a current, it will stop. \n\nThis script will be run on all devices with a .dev file inside the current directory.");
	}
}
