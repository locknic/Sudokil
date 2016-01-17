package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;

public abstract class EntityProcess
{
	protected Entity entity;

	public EntityProcess(Entity entity)
	{
		this.entity = entity;
	}

	public abstract boolean process();

	public void dispose()
	{
		
	}

}
