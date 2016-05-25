package com.custardgames.sudokil.events;

import java.util.UUID;

public class TimerDoneEvent extends BaseEvent
{
	private UUID owner;
	
	public TimerDoneEvent(UUID owner)
	{
		this.owner = owner;
	}
	
	public UUID getOwner()
	{
		return owner;
	}
}
