package com.custardgames.sudokil.events.entities;

import com.artemis.Entity;

public class EntityTurnedEvent extends EntityEvent
{
	private float angle;

	public EntityTurnedEvent(Entity entity, float angle)
	{
		super(entity);
		this.setAngle(angle);
	}

	public float getAngle()
	{
		return angle;
	}

	public void setAngle(float angle)
	{
		this.angle = angle;
	}

}
