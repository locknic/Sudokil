package com.custardgames.sudokil.entities.ecs.components;

public class PowerGeneratorComponent
{
	private int powerPerSecond;

	public PowerGeneratorComponent(int powerPerSecond)
	{
		this.powerPerSecond = powerPerSecond;
	}
	
	public int getPowerPerSecond()
	{
		return powerPerSecond;
	}
	
}
