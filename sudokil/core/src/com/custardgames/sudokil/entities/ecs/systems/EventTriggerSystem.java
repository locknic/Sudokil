package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.triggers.TriggerConditionsComponent;
import com.custardgames.sudokil.entities.ecs.components.triggers.TriggerEntityComponent;
import com.custardgames.sudokil.entities.ecs.components.triggers.TriggerReactionsComponent;
import com.custardgames.sudokil.events.entities.SetTriggerEvent;
import com.custardgames.sudokil.managers.EventManager;

public class EventTriggerSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<EntityComponent> entityComponents;
	private ComponentMapper<TriggerEntityComponent> triggerEntityComponents;
	private ComponentMapper<TriggerConditionsComponent> triggerConditionsComponents;
	private ComponentMapper<TriggerReactionsComponent> triggerReactionsComponents;

	@SuppressWarnings("unchecked")
	public EventTriggerSystem()
	{
		super(Aspect.all(TriggerEntityComponent.class, TriggerConditionsComponent.class, TriggerReactionsComponent.class));

		EventManager.get_instance().register(SetTriggerEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(SetTriggerEvent.class, this);
	}

	@Override
	protected void process(Entity e)
	{
		TriggerEntityComponent triggerEntityComponent = triggerEntityComponents.get(e);
		TriggerConditionsComponent triggerConditionsComponent = triggerConditionsComponents.get(e);
		TriggerReactionsComponent triggerReactionsComponent = triggerReactionsComponents.get(e);

		if (!triggerEntityComponent.isTriggered())
		{
			if (triggerConditionsComponent.isTriggered())
			{
				triggerEntityComponent.setTriggered(true);
				triggerReactionsComponent.triggerReactions();
			}
		}

	}

	public void handleSetTrigger(SetTriggerEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			EntityComponent entityComponent = entityComponents.get(entity);
			if (entityComponent != null)
			{
				String entityID = entityComponent.getId();
				if (event.getEntityName().equals(entityID))
				{
					TriggerConditionsComponent triggerConditionsComponent = triggerConditionsComponents.get(entity);
					if (event.isRunning())
					{
						triggerConditionsComponent.start();
					}
					else
					{
						triggerConditionsComponent.stop();
					}
				}
			}
		}
	}

}
