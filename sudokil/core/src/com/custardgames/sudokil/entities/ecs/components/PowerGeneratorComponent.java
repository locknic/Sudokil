package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class PowerGeneratorComponent extends Component
{
	private boolean generatingPower;

	public PowerGeneratorComponent()
	{
		generatingPower = true;
	}

	public boolean isGeneratingPower()
	{
		return generatingPower;
	}

	public void setGeneratingPower(boolean generatingPower)
	{
		this.generatingPower = generatingPower;
	}
	
	
	
}
