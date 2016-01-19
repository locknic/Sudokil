package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class TextTagComponent extends Component
{
	private boolean shouldRender;
	private String text;
	private float deltaX, deltaY;

	public boolean isShouldRender()
	{
		return shouldRender;
	}

	public void setShouldRender(boolean shouldRender)
	{
		this.shouldRender = shouldRender;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public float getDeltaX()
	{
		return deltaX;
	}

	public void setDeltaX(float deltaX)
	{
		this.deltaX = deltaX;
	}

	public float getDeltaY()
	{
		return deltaY;
	}

	public void setDeltaY(float deltaY)
	{
		this.deltaY = deltaY;
	}
}
