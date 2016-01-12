package com.custardgames.sudokil.events.entities.map;

import com.artemis.Entity;
import com.custardgames.sudokil.events.entities.EntityEvent;

public class RemoveFromMapEvent extends EntityEvent
{
	public RemoveFromMapEvent(Entity entity)
	{
		super(entity);
	}
}
