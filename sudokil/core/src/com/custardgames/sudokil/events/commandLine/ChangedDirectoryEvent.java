package com.custardgames.sudokil.events.commandLine;

import com.custardgames.sudokil.events.BaseEvent;

public class ChangedDirectoryEvent extends BaseEvent
{
	private String directory;

	public ChangedDirectoryEvent(String newDirectory)
	{
		this.directory = newDirectory;
	}

	public String getDirectory()
	{
		return directory;
	}
}
