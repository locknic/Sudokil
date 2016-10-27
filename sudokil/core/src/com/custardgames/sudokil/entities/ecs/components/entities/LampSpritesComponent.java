package com.custardgames.sudokil.entities.ecs.components.entities;

import com.artemis.Component;

public class LampSpritesComponent extends Component
{
	private String lampOn;
	private String lampOff;
	
	public LampSpritesComponent()
	{
		lampOn = "images/entities/lamp_on.png";
		lampOff = "images/entities/lamp_off.png";
	}

	public String getLampOn()
	{
		return lampOn;
	}

	public String getLampOff()
	{
		return lampOff;
	}
}
