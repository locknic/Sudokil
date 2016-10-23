package com.custardgames.sudokil.events.entities.commands;

import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.managers.EventManager;

public class LeftEvent extends EntityCommandEvent
{
	private int num;
	
	@Override
	public void setDefaultDocumentation()
	{
		setName("left.sh - turns robot left");
		setUsage("left.sh [num]");
		setDescription(
				"Turns the robot to the left (counter clockwise) by 90 degrees multiplied by the num provided. If no num provided, the robot will turn 90 degrees counter clockwise. \n\nThis script will be run on all robots with a .dev file inside the current directory. \n\n    [num]: number of times to rotate (positive integer)");
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
			num = 1;
			return true;
		}
		else if (checkIntegerArgs(args))
		{
			num = Integer.parseInt(args[1]);
			if (num > 100)
			{
				num = 99;
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(getOwnerUI(), "WARNING! num reduced to 99 to prevent overflow."));
				return true;
			}
			else if (num > 0)
			{
				return true;
			}
		}

		EventManager.get_instance().broadcast(new ConsoleOutputEvent(getOwnerUI(), "ERROR! Argument must be empty, or a positive integer."));
		EventManager.get_instance().broadcast(new ConsoleOutputEvent(getOwnerUI(), getUsage()));
		return false;
	}

	public int getNum()
	{
		return num;
	}

	public void setNum(int num)
	{
		this.num = num;
	}
}
