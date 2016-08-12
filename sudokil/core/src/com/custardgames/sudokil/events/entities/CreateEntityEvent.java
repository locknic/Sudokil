package com.custardgames.sudokil.events.entities;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.events.BaseEvent;

public class CreateEntityEvent extends BaseEvent
{
	private Array<Component> components;

	public CreateEntityEvent(Array<Component> components)
	{
		super();

		this.components = components;
	}

	public Array<Component> getComponents()
	{
		return components;
	}

}
