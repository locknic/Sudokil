package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class BlockingComponent extends Component
{
	private boolean blocking;

	public boolean isBlocking()
	{
		return blocking;
	}

	public void setBlocking(boolean blocking)
	{
		this.blocking = blocking;
	}
}
