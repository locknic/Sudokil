package com.custardgames.sudokil.events.entities;

import com.artemis.Entity;

public class BlockActivityEvent extends EntityEvent
{
	private Class<?> componentClass;

	public BlockActivityEvent(Entity entity, Class<?> component)
	{
		super(entity);
		this.setComponentClass(component);
	}

	public Class<?> getComponentClass()
	{
		return componentClass;
	}

	public void setComponentClass(Class<?> componentClass)
	{
		this.componentClass = componentClass;
	}
}
