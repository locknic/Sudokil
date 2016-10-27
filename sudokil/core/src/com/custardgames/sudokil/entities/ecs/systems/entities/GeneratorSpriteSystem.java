package com.custardgames.sudokil.entities.ecs.systems.entities;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.custardgames.sudokil.entities.ecs.components.LiftableComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.entities.ecs.components.entities.GeneratorSpritesComponent;

public class GeneratorSpriteSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<LiftableComponent> liftableComponents;
	private ComponentMapper<SpriteComponent> spriteComponents;
	private ComponentMapper<GeneratorSpritesComponent> generatorSpritesComponents;
	
	@SuppressWarnings("unchecked")
	public GeneratorSpriteSystem()
	{
		super(Aspect.all(LiftableComponent.class, SpriteComponent.class, GeneratorSpritesComponent.class));

	}

	@Override
	protected void process(Entity e)
	{
		LiftableComponent liftableComponent = liftableComponents.get(e);
		SpriteComponent spriteComponent = spriteComponents.get(e);
		GeneratorSpritesComponent generatorSpritesComponent = generatorSpritesComponents.get(e);
		
		if (liftableComponent.isLifted())
		{
			spriteComponent.setSpriteLocation(generatorSpritesComponent.getLifted());
		}
		else
		{
			spriteComponent.setSpriteLocation(generatorSpritesComponent.getNormal());
		}
	}

}
