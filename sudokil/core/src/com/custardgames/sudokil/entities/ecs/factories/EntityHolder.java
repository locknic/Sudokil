package com.custardgames.sudokil.entities.ecs.factories;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class EntityHolder
{
	private Array<Array<Component>> entities;

	public Array<Array<Component>> getComponents()
	{
		return entities;
	}
}
