package com.custardgames.sudokil.events;

public class ChangeMapEvent extends BaseEvent
{
	private String newMapLocation;
	
	public String getNewMapLocation()
	{
		return newMapLocation;
	}
}
