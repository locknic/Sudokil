package com.custardgames.sudokil.triggers.conditions;

import java.util.EventListener;

import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.DisposeWorldEvent;
import com.custardgames.sudokil.events.entities.EntityMovedEvent;
import com.custardgames.sudokil.managers.EventManager;

public class AreaTriggerCondition extends BaseTriggerCondition implements EventListener
{
	private boolean triggered;

	private String entityID;
	private float triggerAreaMinX;
	private float triggerAreaMinY;
	private float triggerAreaMaxX;
	private float triggerAreaMaxY;

	public AreaTriggerCondition()
	{
		super();

		EventManager.get_instance().register(DisposeWorldEvent.class, this);
		EventManager.get_instance().register(EntityMovedEvent.class, this);
	}

	@Override
	public boolean checkConditions()
	{
		return triggered;
	}

	public void handleEntityMoved(EntityMovedEvent event)
	{
		if (isRunning())
		{
			EntityComponent entityComponent = event.getEntity().getComponent(EntityComponent.class);
			if (entityComponent.getId().equals(entityID))
			{
				PositionComponent positionComponent = event.getEntity().getComponent(PositionComponent.class);
				System.out.println(positionComponent.getX() + ", " + positionComponent.getY() + " - TRIGGER : " + triggerAreaMinX + "," + triggerAreaMinY
						+ ", MAX : " + triggerAreaMaxX + "," + triggerAreaMaxY);
				if (positionComponent != null && positionComponent.getX() >= triggerAreaMinX && positionComponent.getY() >= triggerAreaMinY
						&& positionComponent.getX() <= triggerAreaMaxX && positionComponent.getY() <= triggerAreaMaxY)
				{
					triggered = true;
				}
				else
				{
					triggered = false;
				}
			}
		}
	}

	public void handleDisposeWorld(DisposeWorldEvent event)
	{
		EventManager.get_instance().deregister(DisposeWorldEvent.class, this);
		EventManager.get_instance().deregister(EntityMovedEvent.class, this);
	}

}
