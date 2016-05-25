package com.custardgames.sudokil.entities.ecs.components.triggers;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.triggers.conditions.BaseTriggerCondition;

public class TriggerConditionsComponent extends Component
{
	private Array<BaseTriggerCondition> triggerConditions;
	
	public TriggerConditionsComponent()
	{
		triggerConditions = new Array<BaseTriggerCondition>();
	}

	public Array<BaseTriggerCondition> getTriggerConditions()
	{
		return triggerConditions;
	}

	public void addTriggerCondition(BaseTriggerCondition triggerConditions)
	{
		this.triggerConditions.add(triggerConditions);
	}
	
	public boolean isTriggered()
	{
		for(BaseTriggerCondition triggerCondition : triggerConditions)
		{
			if (triggerCondition.checkConditions())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isAllTriggered()
	{
		for(BaseTriggerCondition triggerCondition : triggerConditions)
		{
			if (!triggerCondition.checkConditions())
			{
				return false;
			}
		}
		return true;
	}
	
	
}
