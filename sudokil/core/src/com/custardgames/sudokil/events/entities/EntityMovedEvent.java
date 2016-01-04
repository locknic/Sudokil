package com.custardgames.sudokil.events.entities;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.events.BaseEvent;

public class EntityMovedEvent extends BaseEvent
{
	private Entity entity;
	private float deltaX, deltaY;

	public EntityMovedEvent(Entity entity, float deltaX, float deltaY)
	{
		super(entity.getComponent(EntityComponent.class).getId());
		this.setEntity(entity);
		this.setDeltaX(deltaX);
		this.setDeltaY(deltaY);
	}
	
	public Entity getEntity()
	{
		return entity;
	}

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
