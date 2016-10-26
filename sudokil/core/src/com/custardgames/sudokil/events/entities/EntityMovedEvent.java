package com.custardgames.sudokil.events.entities;

import com.artemis.Entity;

public class EntityMovedEvent extends EntityEvent
{
	public EntityMovedEvent(Entity entity)
	{
		super(entity);
	}
}
