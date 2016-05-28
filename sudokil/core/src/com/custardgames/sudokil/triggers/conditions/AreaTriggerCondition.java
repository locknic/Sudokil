package com.custardgames.sudokil.triggers.conditions;

import java.util.EventListener;

import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.DisposeWorldEvent;
import com.custardgames.sudokil.events.entities.EntityMovedEvent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.events.entities.map.RemoveFromMapEvent;
import com.custardgames.sudokil.managers.EventManager;

public class AreaTriggerCondition extends BaseTriggerCondition implements EventListener
{
	private boolean triggered;

	private String entityID;
	private float triggerAreaMinX;
	private float triggerAreaMinY;
	private float triggerAreaMaxX;
	private float triggerAreaMaxY;

	private PositionComponent positionComponent;

	public AreaTriggerCondition()
	{
		super();

		EventManager.get_instance().register(DisposeWorldEvent.class, this);
		EventManager.get_instance().register(AddToMapEvent.class, this);
		EventManager.get_instance().register(RemoveFromMapEvent.class, this);
		EventManager.get_instance().register(EntityMovedEvent.class, this);
	}

	@Override
	public boolean checkConditions()
	{
		return triggered;
	}
	
	public void handleAddToMap(AddToMapEvent event)
	{
		EntityComponent entityComponent = event.getEntity().getComponent(EntityComponent.class);
		if (entityComponent.getId().equals(entityID))
		{
			positionComponent = event.getEntity().getComponent(PositionComponent.class);
		}
	}
	
	public void handleRemoveFromMap(RemoveFromMapEvent event)
	{
		EntityComponent entityComponent = event.getEntity().getComponent(EntityComponent.class);
		if (entityComponent.getId().equals(entityID))
		{
			positionComponent = null;
		}
	}

	public void handleEntityMoved(EntityMovedEvent event)
	{
		EntityComponent entityComponent = event.getEntity().getComponent(EntityComponent.class);
		if (entityComponent.getId().equals(entityID))
		{
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
	
	public void handleDisposeWorld(DisposeWorldEvent event)
	{
		EventManager.get_instance().deregister(DisposeWorldEvent.class, this);
		EventManager.get_instance().deregister(AddToMapEvent.class, this);
		EventManager.get_instance().deregister(RemoveFromMapEvent.class, this);
		EventManager.get_instance().deregister(EntityMovedEvent.class, this);
	}

}
