package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class DoorGroupComponent extends Component
{
	private Array<String> doorTiles;

	public DoorGroupComponent()
	{
		setDoorTiles(new Array<String>());
	}

	public Array<String> getDoorTiles()
	{
		return doorTiles;
	}

	public void setDoorTiles(Array<String> doorTiles)
	{
		this.doorTiles = doorTiles;
	}

}
