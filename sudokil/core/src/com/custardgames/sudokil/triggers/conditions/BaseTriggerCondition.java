package com.custardgames.sudokil.triggers.conditions;

public abstract class BaseTriggerCondition
{
	protected boolean isDone;

	public BaseTriggerCondition()
	{
		isDone = false;
	}

	public abstract boolean checkConditions();
}
