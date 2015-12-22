package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.events.map.AddToMapEvent;
import com.custardgames.sudokil.events.map.RemoveFromMapEvent;
import com.custardgames.sudokil.managers.EventManager;

public class DoorToggleProcess extends EntityProcess
{

	public DoorToggleProcess(Entity entity)
	{
		super(entity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean process()
	{
		SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
		PositionComponent positionComponent = entity.getComponent(PositionComponent.class);

		if (spriteComponent.isShouldRender())
		{
			spriteComponent.setShouldRender(false);
			positionComponent.setSolid(false);
			EventManager.get_instance().broadcast(new RemoveFromMapEvent(entity));
		}
		else
		{
			spriteComponent.setShouldRender(true);
			positionComponent.setSolid(true);
			EventManager.get_instance().broadcast(new AddToMapEvent(entity));
		}

		return true;
	}

}
