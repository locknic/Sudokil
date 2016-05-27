package com.custardgames.sudokil.events;

import com.custardgames.sudokil.ui.cli.RootCLI;

public class PingFileSystemEvent extends BaseEvent
{
	private String assetLocation;
	private RootCLI fileSystem;

	public PingFileSystemEvent(String assetLocation)
	{
		setAssetLocation(assetLocation);
	}

	public String getAssetLocation()
	{
		return assetLocation;
	}

	public void setAssetLocation(String assetLocation)
	{
		this.assetLocation = assetLocation;
	}

	public RootCLI getFileSystem()
	{
		return fileSystem;
	}

	public void setFileSystem(RootCLI fileSystem)
	{
		this.fileSystem = fileSystem;
	}

}
