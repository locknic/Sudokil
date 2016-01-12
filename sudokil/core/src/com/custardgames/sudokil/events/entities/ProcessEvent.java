package com.custardgames.sudokil.events.entities;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.processes.EntityProcess;

public class ProcessEvent extends EntityEvent
{
	private EntityProcess process;

	public ProcessEvent(Entity entity, EntityProcess process)
	{
		super(entity);
		this.setProcess(process);
	}

	public EntityProcess getProcess()
	{
		return process;
	}

	public void setProcess(EntityProcess process)
	{
		this.process = process;
	}
}
