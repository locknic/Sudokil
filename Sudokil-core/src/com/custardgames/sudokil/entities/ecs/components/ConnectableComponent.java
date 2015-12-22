package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class ConnectableComponent extends Component
{
	private String fileLocation;

	public String getFileLocation()
	{
		return fileLocation;
	}

	public void setFileLocation(String fileLocation)
	{
		this.fileLocation = fileLocation;
	}

}
