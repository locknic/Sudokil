package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.ActivityBlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteDeactiveComponent;
import com.custardgames.sudokil.events.entities.PowerStorageEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ActivitySpriteSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<SpriteComponent> spriteComponents;
	private ComponentMapper<SpriteDeactiveComponent> spriteDeactiveComponents;
	private ComponentMapper<ActivityBlockingComponent> activityBlockingComponents;

	@SuppressWarnings("unchecked")
	public ActivitySpriteSystem()
	{
		super(Aspect.all(SpriteComponent.class, SpriteDeactiveComponent.class, ActivityBlockingComponent.class));

		EventManager.get_instance().register(PowerStorageEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();
		
		EventManager.get_instance().deregister(PowerStorageEvent.class, this);
	}

	@Override
	public boolean checkProcessing()
	{
		return true;
	}

	@Override
	protected void process(Entity entity)
	{

	}

	public void checkAndChangeSprite(Entity entity)
	{
		SpriteComponent spriteComponent = spriteComponents.get(entity);
		SpriteDeactiveComponent spriteDeactiveComponent = spriteDeactiveComponents.get(entity);
		ActivityBlockingComponent activityBlockingComponent = activityBlockingComponents.get(entity);

		if (activityBlockingComponent.isActive())
		{
			spriteComponent.setSpriteLocation(spriteDeactiveComponent.getOnSpriteLocation());
		}
		else
		{
			spriteComponent.setSpriteLocation(spriteDeactiveComponent.getOffSpriteLocation());
		}
	}
	
	public void handlePowerStorage(PowerStorageEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			if (entity == event.getEntity())
			{
				checkAndChangeSprite(entity);
			}
		}
	}

}
