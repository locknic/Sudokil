package com.custardgames.sudokil.events.entities.commands.camera;

import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.events.entities.commands.EntityCommandEvent;
import com.custardgames.sudokil.managers.EventManager;

public class CameraTargetEvent extends EntityCommandEvent
{
	private String targetEntity;

	@Override
	public void setDefaultDocumentation()
	{
		setName("target.sh - set camera target");
		setUsage("target.sh [entity_id]");
		setDescription("Set the camera focus to a specific entity on the map. The camera will fix itself to the entity, and follow it around. If the camera is moved, an offset from the entity will be saved. \n\nThis script will be run on all cameras with a .dev file inside the current directory.\n\n    [entity_id]: name of target entity");
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
