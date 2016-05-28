package com.custardgames.sudokil.triggers.conditions;

import com.badlogic.gdx.utils.Array;

public class AndTriggerConditionArray extends BaseTriggerCondition
{
	Array<BaseTriggerCondition> triggerConditions;
	
	public AndTriggerConditionArray()
	{
		triggerConditions = new Array<BaseTriggerCondition>();
	}
	
	public AndTriggerConditionArray(Array<BaseTriggerCondition> triggerConditions)
	{
		this.triggerConditions = triggerConditions;
	}
	
	@Override
	public void start()
	{
		super.start();
		
		for (BaseTriggerCondition trigger : triggerConditions)
		{
			trigger.start();
		}
	}
	
	@Override
	public void stop()
	{
		super.start();
		
		for (BaseTriggerCondition trigger : triggerConditions)
		{
			trigger.stop();
		}
	}
	
	@Override
	public boolean checkConditions()
	{
		for (BaseTriggerCondition trigger : triggerConditions)
		{
			if (!trigger.checkConditions())
			{
				return false;
			}
		}
		return true;
	}

}
