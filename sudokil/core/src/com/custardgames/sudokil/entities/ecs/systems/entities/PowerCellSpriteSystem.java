package com.custardgames.sudokil.entities.ecs.systems.entities;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.custardgames.sudokil.entities.ecs.components.LiftableComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.entities.ecs.components.entities.PowerCellSpritesComponent;

public class PowerCellSpriteSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<LiftableComponent> liftableComponents;
	private ComponentMapper<SpriteComponent> spriteComponents;
	private ComponentMapper<PowerCellSpritesComponent> powerCellSpritesComponents;
	
	@SuppressWarnings("unchecked")
	public PowerCellSpriteSystem()
	{
		super(Aspect.all(LiftableComponent.class, SpriteComponent.class, PowerCellSpritesComponent.class));

	}

	@Override
	protected void process(Entity e)
	{
		LiftableComponent liftableComponent = liftableComponents.get(e);
		SpriteComponent spriteComponent = spriteComponents.get(e);
		PowerCellSpritesComponent powerCellSpritesComponent = powerCellSpritesComponents.get(e);
		
		if (liftableComponent.isLifted())
		{
			spriteComponent.setSpriteLocation(powerCellSpritesComponent.getLifted());
		}
		else
		{
			spriteComponent.setSpriteLocation(powerCellSpritesComponent.getNormal());
		}
	}

}
