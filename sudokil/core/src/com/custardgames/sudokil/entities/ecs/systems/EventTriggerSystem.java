package com.custardgames.sudokil.entities.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.custardgames.sudokil.entities.ecs.components.triggers.TriggerConditionsComponent;
import com.custardgames.sudokil.entities.ecs.components.triggers.TriggerEntityComponent;
import com.custardgames.sudokil.entities.ecs.components.triggers.TriggerReactionsComponent;

public class EventTriggerSystem extends EntityProcessingSystem
{
	private ComponentMapper<TriggerEntityComponent> triggerEntityComponents;
	private ComponentMapper<TriggerConditionsComponent> triggerConditionsComponents;
	private ComponentMapper<TriggerReactionsComponent> triggerReactionsComponents;
	
	@SuppressWarnings("unchecked")
	public EventTriggerSystem()
	{
		super(Aspect.all(TriggerEntityComponent.class, TriggerConditionsComponent.class, TriggerReactionsComponent.class));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(Entity e)
	{
		TriggerEntityComponent triggerEntityComponent = triggerEntityComponents.get(e);
		TriggerConditionsComponent triggerConditionsComponent = triggerConditionsComponents.get(e);
		TriggerReactionsComponent triggerReactionsComponent = triggerReactionsComponents.get(e);
		
		if (!triggerEntityComponent.isTriggered() || triggerEntityComponent.isLooping())
		{
			if(triggerEntityComponent.isNeedsAllConditions())
			{
				if (triggerConditionsComponent.isAllTriggered())
				{
					triggerEntityComponent.setTriggered(true);
					triggerReactionsComponent.triggerReactions();
				}
			}
			else
			{
				if (triggerConditionsComponent.isTriggered())
				{
					triggerEntityComponent.setTriggered(true);
					triggerReactionsComponent.triggerReactions();
				}
			}
		}
		
	}

}
