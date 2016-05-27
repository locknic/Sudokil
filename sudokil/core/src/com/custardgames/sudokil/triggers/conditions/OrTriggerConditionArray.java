package com.custardgames.sudokil.triggers.conditions;

import com.badlogic.gdx.utils.Array;

public class OrTriggerConditionArray extends BaseTriggerCondition
{
	Array<BaseTriggerCondition> triggerConditions;

	public OrTriggerConditionArray()
	{
		triggerConditions = new Array<BaseTriggerCondition>();
	}

	public OrTriggerConditionArray(Array<BaseTriggerCondition> triggerConditions)
	{
		this.triggerConditions = triggerConditions;
	}

	@Override
	public boolean checkConditions()
	{
		for (BaseTriggerCondition trigger : triggerConditions)
		{
			if (trigger.checkConditions())
			{
				return true;
			}
		}
		return false;
	}

}
