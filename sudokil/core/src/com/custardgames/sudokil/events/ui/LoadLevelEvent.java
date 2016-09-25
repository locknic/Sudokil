package com.custardgames.sudokil.events.ui;

import com.custardgames.sudokil.events.BaseEvent;

public class LoadLevelEvent extends BaseEvent
{
	private String fileLocation;

	public LoadLevelEvent(String fileLocation)
	{
		this.fileLocation = fileLocation;	
	}

	public String getFileLocation()
	{
		return fileLocation;
	}

	public void setFileLocation(String fileLocation)
	{
		this.fileLocation = fileLocation;
	}
}
