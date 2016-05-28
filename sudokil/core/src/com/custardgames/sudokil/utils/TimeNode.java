package com.custardgames.sudokil.utils;

import java.util.UUID;

public class TimeNode
{
	private UUID owner;

	private float currentTimer;
	private float duration;

	public TimeNode(UUID owner, float duration)
	{
		this.owner = owner;

		this.duration = duration;
		currentTimer = 0;
	}

	public UUID getOwner()
	{
		return owner;
	}

	public void process(float time)
	{
		currentTimer += time;
	}

	public boolean isFinished()
	{
		if (currentTimer > duration)
		{
			return true;
		}

		return false;
	}
}
