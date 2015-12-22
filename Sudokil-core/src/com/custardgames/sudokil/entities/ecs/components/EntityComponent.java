package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class EntityComponent extends Component
{
	private String id;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
}
