package com.custardgames.sudokil.events.entities.camera;

import com.custardgames.sudokil.events.BaseEvent;

public class CameraMovedEvent extends BaseEvent
{
	private float x;
	private float y;
	
	public CameraMovedEvent()
	{
		x = 0;
		y = 0;
	}
	
	public CameraMovedEvent(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}
}
