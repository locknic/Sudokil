package com.custardgames.sudokil.events.physicalinput;

import com.custardgames.sudokil.events.BaseEvent;

public class KeyReleasedEvent extends BaseEvent
{
	private int keyCode;

	public KeyReleasedEvent(int keyCode)
	{
		this.keyCode = keyCode;
	}

	public int getKeyCode()
	{
		return keyCode;
	}

	public void setKeyCode(int keyCode)
	{
		this.keyCode = keyCode;
	}
}
