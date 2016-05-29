package com.custardgames.sudokil.events.physicalinput;

public class MouseDraggedEvent
{
	private int button;
	private float x;
	private float y;
	
	public MouseDraggedEvent(int button, float x, float y)
	{
		this.setButton(button);
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

	public int getButton()
	{
		return button;
	}

	public void setButton(int button)
	{
		this.button = button;
	}
	
	
}
