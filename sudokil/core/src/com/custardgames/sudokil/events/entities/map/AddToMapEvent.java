package com.custardgames.sudokil.events.entities.map;

import com.artemis.Entity;
import com.custardgames.sudokil.events.entities.EntityEvent;

public class AddToMapEvent extends EntityEvent
{
	private Entity entity;

	public AddToMapEvent(Entity entity)
	{
		super(entity);
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
