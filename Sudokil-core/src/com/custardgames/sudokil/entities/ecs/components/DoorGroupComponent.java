package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;

public class DoorGroupComponent extends Component
{
	private Array<String> doorTiles;
	private Array<Entity> doorTilesEntities;

	public DoorGroupComponent()
	{
		setDoorTiles(new Array<String>());
		setDoorTilesEntities(new Array<Entity>());
	}

	public Array<String> getDoorTiles()
	{
		return doorTiles;
	}

	public void setDoorTiles(Array<String> doorTiles)
	{
		this.doorTiles = doorTiles;
	}

	public Array<Entity> getDoorTilesEntities()
	{
		return doorTilesEntities;
	}

	public void setDoorTilesEntities(Array<Entity> doorTilesEntities)
	{
		this.doorTilesEntities = doorTilesEntities;
	}

}
