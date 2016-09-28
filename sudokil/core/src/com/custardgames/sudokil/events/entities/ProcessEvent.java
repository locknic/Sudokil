package com.custardgames.sudokil.events.entities;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.processes.EntityProcess;

public class ProcessEvent extends EntityEvent
{
	private EntityProcess process;
	private boolean toFront;
	
	public ProcessEvent(Entity entity, EntityProcess process)
	{
		super(entity);
		this.setProcess(process);
		toFront = false;
	}
	
	public ProcessEvent(Entity entity, EntityProcess process, boolean toFront)
	{
		super(entity);
		this.setProcess(process);
		this.toFront = toFront;
	}

	public EntityProcess getProcess()
	{
		return process;
	}

	public void setProcess(EntityProcess process)
	{
		this.process = process;
	}

	public boolean isToFront()
	{
		return toFront;
	}

	public void setToFront(boolean toFront)
	{
		this.toFront = toFront;
	}
}
