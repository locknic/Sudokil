package com.custardgames.sudokil.events.physicalinput;

import com.custardgames.sudokil.events.BaseEvent;

public class KeyPressedEvent extends BaseEvent
{
	private int keyCode;

	public KeyPressedEvent(int keyCode)
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
