package com.custardgames.sudokil.entities.ecs.components.triggers;

import com.artemis.Component;
import com.custardgames.sudokil.triggers.conditions.BaseTriggerCondition;

public class TriggerConditionsComponent extends Component
{
	private BaseTriggerCondition triggerCondition;

	public BaseTriggerCondition getTriggerCondition()
	{
		return triggerCondition;
	}

	public void start()
	{
		triggerCondition.start();
	}

	public void stop()
	{
		triggerCondition.stop();
	}

	public boolean isTriggered()
	{
		return triggerCondition.checkConditions();
	}

}
