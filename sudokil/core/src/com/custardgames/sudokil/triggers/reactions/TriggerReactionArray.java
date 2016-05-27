package com.custardgames.sudokil.triggers.reactions;

import com.badlogic.gdx.utils.Array;

public class TriggerReactionArray extends BaseTriggerReaction
{
	private Array<BaseTriggerReaction> triggerReactions;

	public TriggerReactionArray()
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

	@Override
	public void initialiseReaction()
	{
		for (BaseTriggerReaction triggerReaction : triggerReactions)
		{
			triggerReaction.initialiseReaction();
		}
	}
}
