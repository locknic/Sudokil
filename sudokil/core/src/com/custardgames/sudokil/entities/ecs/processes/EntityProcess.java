package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;

public abstract class EntityProcess
{
	protected Entity entity;
	private boolean backgroundProcess;

	public EntityProcess(Entity entity)
	{
		this.entity = entity;
		this.backgroundProcess = false;
	}

	public abstract boolean process();

	public void dispose()
	{

	}

	public boolean isBackgroundProcess()
	{
		return backgroundProcess;
	}

	public void setBackgroundProcess(boolean backgroundProcess)
	{
		this.backgroundProcess = backgroundProcess;
	}

}
