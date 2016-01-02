package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class PowerStorageComponent extends Component
{
	private int maxPowerStorage;
	private int powerStorage;
	
	public PowerStorageComponent(int maxPowerStorage)
	{
		this(maxPowerStorage, 0);
	}
	
	public PowerStorageComponent(int maxPowerStorage, int powerStorage)
	{
		this.maxPowerStorage = maxPowerStorage;
		this.powerStorage = powerStorage;
	}

	public int getMaxPowerStorage()
	{
		return maxPowerStorage;
	}

	public void setMaxPowerStorage(int maxPowerStorage)
	{
		this.maxPowerStorage = maxPowerStorage;
	}

	public int getPowerStorage()
	{
		return powerStorage;
	}

	public void setPowerStorage(int powerStorage)
	{
		this.powerStorage = powerStorage;
	}
}
