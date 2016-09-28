package com.custardgames.sudokil.events;

public class ChangeLevelEvent extends BaseEvent
{
	private String levelDataLocation;

	public ChangeLevelEvent()
	{
		
	}
	
	public ChangeLevelEvent(String levelDataLocation)
	{
		this.levelDataLocation = levelDataLocation;
	}
	
	public String getLevelDataLocation()
	{
		return levelDataLocation;
	}
}
