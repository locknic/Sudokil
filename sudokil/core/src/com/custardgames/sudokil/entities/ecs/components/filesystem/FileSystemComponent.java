package com.custardgames.sudokil.entities.ecs.components.filesystem;

import com.artemis.Component;

public class FileSystemComponent extends Component
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