package com.custardgames.sudokil.events.entities;

import com.artemis.Entity;
import com.custardgames.sudokil.events.BaseEvent;

public class EntityEvent extends BaseEvent
{
	private Entity entity;
	
	public EntityEvent(Entity entity)
	{
		this.setEntity(entity);
	}

	public Entity getEntity()
	{
		return entity;
	}

	public void setEntity(Entity entity)
	{
		this.entity = entity;
	}
}
