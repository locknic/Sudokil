package com.custardgames.sudokil.events.entities;

import com.artemis.Entity;

public class EntityTurnedEvent extends EntityEvent
{
	public EntityTurnedEvent(Entity entity)
	{
		super(entity);
	}
}
