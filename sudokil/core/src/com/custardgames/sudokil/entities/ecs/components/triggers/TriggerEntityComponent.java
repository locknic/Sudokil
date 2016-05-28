package com.custardgames.sudokil.entities.ecs.components.triggers;

import com.artemis.Component;

public class TriggerEntityComponent extends Component
{
	private boolean triggered;

	public TriggerEntityComponent()
	{
		triggered = false;
	}

	public boolean isTriggered()
	{
		return triggered;
	}

	public void setTriggered(boolean triggered)
	{
		this.triggered = triggered;
	}
}
