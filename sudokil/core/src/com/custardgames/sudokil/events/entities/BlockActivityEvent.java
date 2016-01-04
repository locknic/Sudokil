package com.custardgames.sudokil.events.entities;

import com.artemis.Component;
import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.events.BaseEvent;

public class BlockActivityEvent extends BaseEvent
{
	private Component component;
	
	public BlockActivityEvent(Entity entity, Component component)
	{
		super(entity.getComponent(EntityComponent.class).getId());
		this.setComponent(component);
	}

	public Component getComponent()
	{
		return component;
	}

	public void setComponent(Component component)
	{
		this.component = component;
	}
}
