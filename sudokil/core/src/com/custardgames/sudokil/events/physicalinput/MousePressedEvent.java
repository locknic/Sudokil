package com.custardgames.sudokil.events.physicalinput;

import com.custardgames.sudokil.events.BaseEvent;

public class MousePressedEvent extends BaseEvent
{
	private int buttonNumber;
	private float x;
	private float y;

	public MousePressedEvent(int buttonNumber, float x, float y)
	{
		this.setButtonNumber(buttonNumber);
		this.setX(x);
		this.setY(y);
	}

	public int getButtonNumber()
	{
		return buttonNumber;
	}

	public void setButtonNumber(int buttonNumber)
	{
		this.buttonNumber = buttonNumber;
	}

	public float getX()
	{
		return x;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public float getY()
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
	}

}
