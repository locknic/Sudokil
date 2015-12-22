package com.custardgames.sudokil.events.map;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.events.BaseEvent;

public class RemoveFromMapEvent extends BaseEvent
{
	private Entity entity;

	public RemoveFromMapEvent(Entity entity)
	{
		super(entity.getComponent(EntityComponent.class).getId());
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
