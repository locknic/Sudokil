package com.custardgames.sudokil.events.entities.camera;

import com.custardgames.sudokil.events.BaseEvent;

public class CameraZoomedEvent extends BaseEvent
{
	private float zoom;

	public CameraZoomedEvent()
	{
		this.zoom = 0;
	}

	public CameraZoomedEvent(float zoom)
	{
		this.zoom = zoom;
	}

	public float getZoom()
	{
		return zoom;
	}

}
