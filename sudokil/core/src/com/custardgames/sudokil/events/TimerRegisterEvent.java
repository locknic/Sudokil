package com.custardgames.sudokil.events;

import java.util.UUID;

public class TimerRegisterEvent extends BaseEvent
{
	private UUID owner;
	private float duration;

	public TimerRegisterEvent(UUID owner, float duration)
	{
		this.owner = owner;
		this.duration = duration;
	}

	public UUID getOwner()
	{
		return owner;
	}

	public float getDuration()
	{
		return duration;
	}

}
