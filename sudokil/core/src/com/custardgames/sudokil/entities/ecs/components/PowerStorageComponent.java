package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class PowerStorageComponent extends Component
{
	private boolean powered;
	
	public PowerStorageComponent()
	{
		powered = false;
	}

	public boolean isPowered()
	{
		return powered;
	}

	public void setPowered(boolean powered)
	{
		this.powered = powered;
	}
}
