package com.custardgames.sudokil.entities.ecs.systems.entities;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.custardgames.sudokil.entities.ecs.components.PowerConsumerComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.entities.ecs.components.entities.LampSpritesComponent;

public class LampSpriteSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<PowerConsumerComponent> powerConsumerComponents;
	private ComponentMapper<SpriteComponent> spriteComponents;
	private ComponentMapper<LampSpritesComponent> lampSpritesComponents;
	

	public LampSpriteSystem()
	{
		super(Aspect.all(PowerConsumerComponent.class, SpriteComponent.class, LampSpritesComponent.class));

	}

	@Override
	protected void process(Entity e)
	{
		PowerConsumerComponent powerConsumerComponent = powerConsumerComponents.get(e);
		SpriteComponent spriteComponent = spriteComponents.get(e);
		LampSpritesComponent lampSpritesComponent = lampSpritesComponents.get(e);
		
		if(powerConsumerComponent.isPowered())
		{
			spriteComponent.setSpriteLocation(lampSpritesComponent.getLampOn());
		}
		else
		{
			spriteComponent.setSpriteLocation(lampSpritesComponent.getLampOff());
		}
		
	}

}
