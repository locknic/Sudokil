package com.custardgames.sudokil.triggers.conditions;

import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.PingEntityEvent;
import com.custardgames.sudokil.managers.EventManager;

public class AreaTriggerCondition extends BaseTriggerCondition
{
	private String entityID;
	private float triggerAreaMinX;
	private float triggerAreaMinY;
	private float triggerAreaMaxX;
	private float triggerAreaMaxY;
	
	private PositionComponent positionComponent;
	
	public AreaTriggerCondition()
	{
		super();
	}
	
	@Override
	public boolean checkConditions()
	{
		if (positionComponent == null)
		{
			PingEntityEvent event = (PingEntityEvent) EventManager.get_instance()
					.broadcastInquiry(new PingEntityEvent(entityID));
			if (event != null && event instanceof PingEntityEvent)
			{
				positionComponent = event.getEntity().getComponent(PositionComponent.class);
			}
		}
		if (positionComponent != null && positionComponent.getX() >= triggerAreaMinX && positionComponent.getY() >= triggerAreaMinY && positionComponent.getX() <= triggerAreaMaxX && positionComponent.getY() <= triggerAreaMaxY)
		{
			System.out.println("WORKED");
			return true;
		}
		return false;
	}

}
