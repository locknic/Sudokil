package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class PowerConsumerComponent extends Component
{
	private boolean powered;

	public boolean isPowered()
	{
		return powered;
	}

	public void setPowered(boolean powered)
	{
		this.powered = powered;
	}
}
