package com.custardgames.sudokil.ui.tools;

import java.util.UUID;

import com.badlogic.gdx.utils.Array;
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
		UUID previousUI = null;
		if (previousCommandLineData != null)
		{
			previousUI = previousCommandLineData.ownerUI;
		}
		this.parser = new CommandLineManager(root, ownerUI, previousUI);
		this.previousCommandLineData = previousCommandLineData;
	}

	public CommandLineData findCommandLineData(UUID id)
	{
		if (id.equals(ownerUI))
		{
			return this;
		}
		else if (previousCommandLineData != null)
		{
			return previousCommandLineData.findCommandLineData(id);
		}
		else
		{
			return null;
		}
	}

	public Array<UUID> getAllUUIDs()
	{
		Array<UUID> uuids;
		if (previousCommandLineData != null)
		{
			uuids = previousCommandLineData.getAllUUIDs();
		}
		else
		{
			uuids = new Array<UUID>();
		}
		uuids.add(ownerUI);
		return uuids;
	}
}
