package com.custardgames.sudokil.triggers.conditions;

public abstract class BaseTriggerCondition
{
	protected boolean isDone;
	private boolean running;

	public BaseTriggerCondition()
	{
		isDone = false;
		running = false;
	}

	public void start()
	{
		running = true;
	}

	public void stop()
	{
		running = false;
	}

	public boolean isRunning()
	{
		return running;
	}

	public abstract boolean checkConditions();
}
