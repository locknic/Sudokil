package com.custardgames.sudokil.states;

import com.badlogic.gdx.utils.Array;

public class LevelData
{
	private String mapLocation;
	private Array<String> entities;
	private String playerFilesystem;
	private Array<String> filesystems;
	private Array<String> images;

	public String getMapLocation()
	{
		return mapLocation;
	}

	public Array<String> getEntities()
	{
		return entities;
	}
	
	public String getPlayerFilesystem()
	{
		return playerFilesystem;
	}

	public Array<String> getFilesystems()
	{
		return filesystems;
	}

	public Array<String> getImages()
	{
		return images;
	}

	
}
