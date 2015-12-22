package com.custardgames.sudokil.events.physicalinput;

import com.custardgames.sudokil.events.BaseEvent;

public class MouseWheelMovedEvent extends BaseEvent
{
	private int mouseWheelAmount;

	public MouseWheelMovedEvent(int amount)
	{
		this.setMouseWheelAmount(amount);
	}

	public int getMouseWheelAmount()
	{
		return mouseWheelAmount;
	}

	public void setMouseWheelAmount(int mouseWheelAmount)
	{
		this.mouseWheelAmount = mouseWheelAmount;
	}
}
