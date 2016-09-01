package com.custardgames.sudokil.ui;

import java.util.UUID;

import com.custardgames.sudokil.managers.CommandLineManager;
import com.custardgames.sudokil.ui.cli.RootCLI;
import com.custardgames.sudokil.utils.CircularArray;

public class CommandLineData
{
	public UUID ownerUI;
	public CommandLineManager parser;
	public String textHistory;
	public CircularArray<String> previousCommands;
	public String tempStore;
	public int commandLocation;
	public CommandLineData previousCommandLineData;
	
	public CommandLineData(RootCLI root, CommandLineData previousCommandLineData)
	{
		this.ownerUI = UUID.randomUUID();
		this.commandLocation = -1;
		this.tempStore = "";
		this.previousCommands = new CircularArray<String>(10);
		this.parser = new CommandLineManager(root, ownerUI);
		this.previousCommandLineData = previousCommandLineData;
	}
	
	public CommandLineData findCommandLineDataParent(UUID id)
	{
		if (id.equals(ownerUI))
		{
			return previousCommandLineData;
		}
		else if (previousCommandLineData != null)
		{
			return previousCommandLineData.findCommandLineDataParent(id);
		}
		else
		{
			return null; 
		}
	}
}
