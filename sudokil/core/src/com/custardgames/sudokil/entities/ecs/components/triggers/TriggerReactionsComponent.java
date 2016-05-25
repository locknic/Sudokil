package com.custardgames.sudokil.entities.ecs.components.triggers;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.triggers.reactions.BaseTriggerReaction;

public class TriggerReactionsComponent extends Component
{
	private Array<BaseTriggerReaction> triggerReactions;

	public TriggerReactionsComponent()
	{
		this.triggerReactions = new Array<BaseTriggerReaction>();
	}
	
	public Array<BaseTriggerReaction> getTriggerReactions()
	{
		return triggerReactions;
	}

	public void addTriggerReaction(BaseTriggerReaction triggerReaction)
	{
		this.triggerReactions.add(triggerReaction);
	}
	
	public void triggerReactions()
	{
		for (BaseTriggerReaction triggerReaction : triggerReactions)
		{
			triggerReaction.initialiseReaction();
		}
	}
}
