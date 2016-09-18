package com.custardgames.sudokil.events.entities;

import com.artemis.Entity;

public class EntityMovedEvent extends EntityEvent
{
	private Entity entity;
	private float deltaX, deltaY;

	public EntityMovedEvent(Entity entity, float deltaX, float deltaY)
	{
		super(entity);
		this.setEntity(entity);
		this.setDeltaX(deltaX);
		this.setDeltaY(deltaY);
	}

	@Override
	public Entity getEntity()
	{
		return entity;
	}

	@Override
	public void setEntity(Entity entity)
	{
		this.entity = entity;
	}

	public float getDeltaX()
	{
		return deltaX;
	}

	public void setDeltaX(float deltaX)
	{
		this.deltaX = deltaX;
	}

	public float getDeltaY()
	{
		return deltaY;
	}

	public void setDeltaY(float deltaY)
	{
		this.deltaY = deltaY;
	}
}
