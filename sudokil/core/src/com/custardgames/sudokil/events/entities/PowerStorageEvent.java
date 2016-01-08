package com.custardgames.sudokil.events.entities;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.events.BaseEvent;

public class PowerStorageEvent extends BaseEvent
{

	public PowerStorageEvent(Entity entity)
	{
		super(entity.getComponent(EntityComponent.class).getId());
	}

	
}
