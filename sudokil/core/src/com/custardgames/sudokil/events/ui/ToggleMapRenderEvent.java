package com.custardgames.sudokil.events.ui;

import com.custardgames.sudokil.events.BaseEvent;

public class ToggleMapRenderEvent extends BaseEvent
{
	private boolean shouldRender;
	
	public ToggleMapRenderEvent()
	{
		shouldRender = false;
	}
	
	public ToggleMapRenderEvent(boolean shouldRender)
	{
		this.shouldRender = shouldRender;
	}
	
	public boolean isShouldRender()
	{
		return shouldRender;
	}
}
