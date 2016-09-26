package com.custardgames.sudokil.entities.ecs.components.current;

import com.artemis.Component;

public class CurrentConsumerComponent extends Component
{
	private boolean hasCurrent;
	
	public CurrentConsumerComponent()
	{
		hasCurrent = false;
	}

	public boolean isHasCurrent()
	{
		return hasCurrent;
	}

	public void setHasCurrent(boolean hasCurrent)
	{
		this.hasCurrent = hasCurrent;
	}
}
