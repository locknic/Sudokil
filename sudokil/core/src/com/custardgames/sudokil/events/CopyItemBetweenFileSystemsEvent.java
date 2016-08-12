package com.custardgames.sudokil.events;

public class CopyItemBetweenFileSystemsEvent extends BaseEvent
{
	private String sourceFileSystem;
	private String targetFileSystem;
	private String sourceItemLocation;
	private String targetItemLocation;

	public String getSourceFileSystem()
	{
		return sourceFileSystem;
	}

	public String getTargetFileSystem()
	{
		return targetFileSystem;
	}

	public String getSourceItemLocation()
	{
		return sourceItemLocation;
	}

	public String getTargetItemLocation()
	{
		return targetItemLocation;
	}

}
