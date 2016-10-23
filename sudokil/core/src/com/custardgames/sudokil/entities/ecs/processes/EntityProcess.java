package com.custardgames.sudokil.entities.ecs.processes;

import java.util.UUID;

import com.artemis.Entity;
import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.managers.EventManager;

public abstract class EntityProcess
{
	protected Entity entity;
	private boolean backgroundProcess;
	
	protected UUID outputUUID;

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
	
	public void setOutputUUID(UUID outputUUID)
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
