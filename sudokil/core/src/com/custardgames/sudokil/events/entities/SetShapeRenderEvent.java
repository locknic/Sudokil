package com.custardgames.sudokil.events.entities;

import com.custardgames.sudokil.events.BaseEvent;

public class SetShapeRenderEvent extends BaseEvent
{
	private String entityID;
	private boolean shouldRender;

	public boolean isShouldRender()
	{
		return shouldRender;
	}

	public void setShouldRender(boolean shouldRender)
	{
		this.shouldRender = shouldRender;
	}

	public String getEntityID()
	{
		return entityID;
	}

	public void setEntityID(String entityID)
	{
		this.entityID = entityID;
	}

}
