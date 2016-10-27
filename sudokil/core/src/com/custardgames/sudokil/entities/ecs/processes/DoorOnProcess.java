package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.BlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.managers.EventManager;

public class DoorOnProcess extends EntityProcess
{
	private float countDown = 10;
	
	private SpriteComponent spriteComponent;
	private BlockingComponent blockingComponent;
	
	public DoorOnProcess(Entity entity)
	{
		super(entity);
		
		spriteComponent = entity.getComponent(SpriteComponent.class);
		blockingComponent = entity.getComponent(BlockingComponent.class);
	}

	@Override
	public boolean preProcess()
	{
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean process()
	{
		boolean finished = true;
		
		if (!spriteComponent.isShouldRender() || !blockingComponent.isBlocking())
		{
			countDown--;
			if (countDown <= 0)
			{
				spriteComponent.setShouldRender(true);
				blockingComponent.setBlocking(true);
				EventManager.get_instance().broadcast(new AddToMapEvent(entity));
			}
			else
			{
				finished = false;
			}
		}
		
		return finished;
	}

	@Override
	public void postProcess()
	{
		// TODO Auto-generated method stub
		
	}

}
