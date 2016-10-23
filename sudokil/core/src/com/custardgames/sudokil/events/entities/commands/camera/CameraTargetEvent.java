package com.custardgames.sudokil.events.entities.commands.camera;

import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.events.entities.commands.EntityCommandEvent;
import com.custardgames.sudokil.managers.EventManager;

public class CameraTargetEvent extends EntityCommandEvent
{
	private String targetEntity;

	@Override
	public void setDefaultUsage()
	{
		setUsage("target.sh entity_id \n\t entity_id: name of target entity");
	}
	
	@Override
	public boolean setArgs(String args[])
	{
		if (checkHelpArgs(args))
		{
			return false;
		}
		if (!checkEmptyArgs(args))
		{
			targetEntity = args[1];
		}

		EventManager.get_instance().broadcast(new ConsoleOutputEvent(getOwnerUI(), "ERROR! Argument must be a valid entity."));
		EventManager.get_instance().broadcast(new ConsoleOutputEvent(getOwnerUI(), getUsage()));
		return false;
	}

	public String getTargetEntity()
	{
		return targetEntity;
	}

	public void setTargetEntity(String targetEntity)
	{
		this.targetEntity = targetEntity;
	}

}
