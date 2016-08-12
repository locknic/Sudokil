package com.custardgames.sudokil.events.entities;

import com.custardgames.sudokil.events.BaseEvent;

public class SetTriggerEvent extends BaseEvent
{
	private boolean running;
	private String entityName;

	public String getEntityName()
	{
		return entityName;
	}

	public boolean isRunning()
	{
		return running;
	}

}
