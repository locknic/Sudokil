package com.custardgames.sudokil.events.entities.commands.camera;

import com.custardgames.sudokil.events.entities.commands.EntityCommandEvent;

public class CameraResetEvent extends EntityCommandEvent
{
	@Override
	public void setDefaultDocumentation()
	{
		setName("reset.sh - reset the camera");
		setUsage("reset.sh");
		setDescription("Resets the camera to it's default zoom and position.\n\nThis script will be run on all cameras with a .dev file inside the current directory.");
	}

}
