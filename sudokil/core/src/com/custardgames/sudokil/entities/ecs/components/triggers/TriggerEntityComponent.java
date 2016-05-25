package com.custardgames.sudokil.entities.ecs.components.triggers;

import com.artemis.Component;

public class TriggerEntityComponent extends Component
{
	private boolean triggered;
	private boolean looping;
	private boolean needsAllConditions;
	
	public TriggerEntityComponent()
	{
		triggered = false;
		looping = false;
		needsAllConditions = false;
	}

	public boolean isLooping()
	{
		return looping;
	}

	public void setLooping(boolean looping)
	{
		this.looping = looping;
	}

	public boolean isNeedsAllConditions()
	{
		return needsAllConditions;
	}

	public void setNeedsAllConditions(boolean needsAllConditions)
	{
		this.needsAllConditions = needsAllConditions;
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
