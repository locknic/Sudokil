package com.custardgames.sudokil.events.physicalinput;

import com.custardgames.sudokil.events.BaseEvent;

public class MousePressedEvent extends BaseEvent
{
	private int buttonNumber;

	public MousePressedEvent(int buttonNumber)
	{
		this.setButtonNumber(buttonNumber);
	}

	public int getButtonNumber()
	{
		return buttonNumber;
	}

	public void setButtonNumber(int buttonNumber)
	{
		this.buttonNumber = buttonNumber;
	}

}
