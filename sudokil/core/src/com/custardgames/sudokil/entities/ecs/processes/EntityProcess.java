package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.utils.Streams;

public abstract class EntityProcess
{
	protected Entity entity;
	private boolean backgroundProcess;

	protected Streams outputUUID;

	private boolean hasPreProcessed;

	public EntityProcess(Entity entity)
	{
		this.entity = entity;
		this.backgroundProcess = false;
	}

	public boolean totalProcess()
	{
		boolean finished = false;

		if (!hasPreProcessed)
		{
			finished = !preProcess();
			hasPreProcessed = true;
		}

		if (!finished)
		{
			finished = process();
			
			if (finished)
			{
				postProcess();
			}
		}
		
		return finished;
	}

	protected abstract boolean preProcess();

	protected abstract boolean process();

	protected abstract void postProcess();

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

	public void setOutputUUID(Streams outputUUID)
	{
		this.outputUUID = outputUUID;
	}

	public void sendOutput(String text)
	{
		if (outputUUID != null)
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(outputUUID, text));
		}
	}

}
