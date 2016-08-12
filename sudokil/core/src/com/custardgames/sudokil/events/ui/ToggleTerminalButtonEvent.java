package com.custardgames.sudokil.events.ui;

import com.custardgames.sudokil.events.BaseEvent;

public class ToggleTerminalButtonEvent extends BaseEvent
{
	private boolean buttonVisible;

	public ToggleTerminalButtonEvent()
	{
		this.buttonVisible = false;
	}

	public ToggleTerminalButtonEvent(boolean buttonVisible)
	{
		this.buttonVisible = buttonVisible;
	}

	public boolean isButtonVisible()
	{
		return buttonVisible;
	}
}
