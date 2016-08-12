package com.custardgames.sudokil.events;

public class ChangeLevelEvent extends BaseEvent
{
	private String levelDataLocation;

	public String getLevelDataLocation()
	{
		return levelDataLocation;
	}
}
