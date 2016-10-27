package com.custardgames.sudokil.entities.ecs.components.entities;

import com.artemis.Component;

public class GeneratorSpritesComponent extends Component
{
	private String normal;
	private String lifted;
	
	public GeneratorSpritesComponent()
	{
		this.normal = "images/entities/power_generator_on.png";
		this.lifted = "images/entities/power_generator_on_levitation.png";
	}

	public String getNormal()
	{
		return normal;
	}

	public String getLifted()
	{
		return lifted;
	}
}
