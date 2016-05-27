package com.custardgames.sudokil.entities.ecs.components.triggers;

import com.artemis.Component;
import com.custardgames.sudokil.triggers.reactions.TriggerReactionArray;

public class TriggerReactionsComponent extends Component
{
	private TriggerReactionArray triggerReaction;
	
	public void triggerReactions()
	{
		if (triggerReaction != null)
		{
			triggerReaction.initialiseReaction();
		}
	}
}
