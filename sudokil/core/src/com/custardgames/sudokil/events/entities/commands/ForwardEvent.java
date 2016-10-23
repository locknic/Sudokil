package com.custardgames.sudokil.events.entities.commands;

import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ForwardEvent extends EntityCommandEvent
{
	private int distance;

	@Override
	public void setDefaultDocumentation()
	{
		setName("forward.sh - move robot forward");
		setUsage("forward.sh [distance]");
		setDescription(
				"Moves robot forward by the specified distance. If no distance is specified, the default distance will be 1 unit. \n\nThis script will be run on all robots with a .dev file inside the current directory. \n\n    [distance]: distance to move (positive integer)");
	}

	@Override
	public boolean setArgs(String args[])
	{
		if (checkHelpArgs(args))
		{
			return false;
		}
		else if (checkEmptyArgs(args))
		{
			distance = 1;
			return true;
		}
		else if (checkIntegerArgs(args))
		{
			distance = Integer.parseInt(args[1]);
			if (distance > 100)
			{
				distance = 99;
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(getOwnerUI(), "WARNING! distance reduced to 99 to prevent overflow."));
				return true;
			}
			else if (distance > 0)
			{
				return true;
			}
		}

		EventManager.get_instance().broadcast(new ConsoleOutputEvent(getOwnerUI(), "ERROR! Argument must be empty, or a positive integer."));
		EventManager.get_instance().broadcast(new ConsoleOutputEvent(getOwnerUI(), getUsage()));
		return false;
	}

	public int getDistance()
	{
		return distance;
	}

	public void setDistance(int distance)
	{
		this.distance = distance;
	}
}
