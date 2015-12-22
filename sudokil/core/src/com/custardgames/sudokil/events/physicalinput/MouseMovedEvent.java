package com.custardgames.sudokil.events.physicalinput;

import com.custardgames.sudokil.events.BaseEvent;

public class MouseMovedEvent extends BaseEvent
{
	private int targetX, targetY;

	public MouseMovedEvent(int targetX, int targetY)
	{
		setMouseEvent(targetX, targetY);
	}

	public int getMouseX()
	{
		return targetX;
	}

	public int getMouseY()
	{
		return targetY;
	}

	public void setMouseEvent(int targetX, int targetY)
	{
		this.targetX = targetX;
		this.targetY = targetY;
	}
}
