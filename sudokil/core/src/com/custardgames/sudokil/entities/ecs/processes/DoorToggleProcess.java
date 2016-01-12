package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.BlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.events.entities.map.RemoveFromMapEvent;
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
		BlockingComponent blockingComponent = entity.getComponent(BlockingComponent.class);

		if (spriteComponent.isShouldRender())
		{
			spriteComponent.setShouldRender(false);
			blockingComponent.setBlocking(false);
			EventManager.get_instance().broadcast(new RemoveFromMapEvent(entity));
		}
		else
		{
			spriteComponent.setShouldRender(true);
			blockingComponent.setBlocking(true);
			EventManager.get_instance().broadcast(new AddToMapEvent(entity));
		}

		return true;
	}

}
