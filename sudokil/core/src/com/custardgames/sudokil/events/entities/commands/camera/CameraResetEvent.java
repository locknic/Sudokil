package com.custardgames.sudokil.events.entities.commands.camera;

import com.custardgames.sudokil.events.entities.commands.EntityCommandEvent;

public class CameraResetEvent extends EntityCommandEvent
{
	@Override
	public void setDefaultUsage()
	{
		setUsage("reset.sh");
	}

}
