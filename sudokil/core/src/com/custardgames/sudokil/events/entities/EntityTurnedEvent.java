package com.custardgames.sudokil.events.entities;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.events.BaseEvent;

public class EntityTurnedEvent extends BaseEvent
{
	private Entity entity;
	private float angle;

	public EntityTurnedEvent(Entity entity, float angle)
	{
		super(entity.getComponent(EntityComponent.class).getId());
		this.setEntity(entity);
		this.setAngle(angle);
	}
	
	public Entity getEntity()
	{
		return entity;
	}

	public void setEntity(Entity entity)
	{
		this.entity = entity;
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
