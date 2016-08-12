package com.custardgames.sudokil.triggers.reactions;

import com.custardgames.sudokil.events.BaseEvent;
import com.custardgames.sudokil.managers.EventManager;

public class BroadcastEventReaction extends BaseTriggerReaction
{
	private BaseEvent event;

	@Override
	public void initialiseReaction()
	{
		EventManager.get_instance().broadcast(event);
	}
}
