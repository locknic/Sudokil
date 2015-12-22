package com.custardgames.sudokil.events.physicalinput;

import com.custardgames.sudokil.events.BaseEvent;

public class MouseReleasedEvent extends BaseEvent
{
	private int buttonNumber;

	public MouseReleasedEvent(int buttonNumber)
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
