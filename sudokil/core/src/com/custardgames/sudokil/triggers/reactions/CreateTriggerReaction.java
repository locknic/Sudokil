package com.custardgames.sudokil.triggers.reactions;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.events.entities.CreateEntityEvent;
import com.custardgames.sudokil.managers.EventManager;

public class CreateTriggerReaction extends BaseTriggerReaction
{
	private Array<Component> components;

	@Override
	public void initialiseReaction()
	{
		EventManager.get_instance().broadcast(new CreateEntityEvent(components));
	}

}
