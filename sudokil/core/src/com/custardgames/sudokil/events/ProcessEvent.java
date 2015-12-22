package com.custardgames.sudokil.events;

import com.custardgames.sudokil.entities.ecs.processes.EntityProcess;

public class ProcessEvent extends BaseEvent
{
	private EntityProcess process;

	public ProcessEvent(String owner, EntityProcess process)
	{
		super(owner);
		this.process = process;
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
