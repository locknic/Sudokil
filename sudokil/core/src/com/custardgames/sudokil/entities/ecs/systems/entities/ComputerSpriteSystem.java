package com.custardgames.sudokil.entities.ecs.systems.entities;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.PowerConsumerComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.entities.ecs.components.entities.ComputerSpritesComponent;

public class ComputerSpriteSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<PositionComponent> positionComponents;
	private ComponentMapper<PowerConsumerComponent> powerConsumerComponents;
	private ComponentMapper<SpriteComponent> spriteComponents;
	private ComponentMapper<ComputerSpritesComponent> computerSpritesComponents;
	
	@SuppressWarnings("unchecked")
	public ComputerSpriteSystem()
	{
		super(Aspect.all(PositionComponent.class, PowerConsumerComponent.class, SpriteComponent.class, ComputerSpritesComponent.class));

	}

	@Override
	protected void process(Entity e)
	{
		PositionComponent positionComponent = positionComponents.get(e);
		PowerConsumerComponent powerConsumerComponent = powerConsumerComponents.get(e);
		SpriteComponent spriteComponent = spriteComponents.get(e);
		ComputerSpritesComponent computerSpritesComponent = computerSpritesComponents.get(e);
		
		float angle = positionComponent.getNormalAngle();
		
		if (angle >= 225 && angle < 315)
		{
			if (powerConsumerComponent.isPowered())
			{
				spriteComponent.setSpriteLocation(computerSpritesComponent.getFrontOn());
			}
			else
			{
				spriteComponent.setSpriteLocation(computerSpritesComponent.getFrontOff());
			}
		}
		else if (angle >= 135 && angle < 315)
		{
			if (powerConsumerComponent.isPowered())
			{
				spriteComponent.setSpriteLocation(computerSpritesComponent.getLeftOn());
			}
			else
			{
				spriteComponent.setSpriteLocation(computerSpritesComponent.getLeftOff());
			}
		}
		else if (angle >= 45 && angle < 315)
		{
			if (powerConsumerComponent.isPowered())
			{
				spriteComponent.setSpriteLocation(computerSpritesComponent.getBackOn());
			}
			else
			{
				spriteComponent.setSpriteLocation(computerSpritesComponent.getBackOff());
			}
		}
		else
		{
			if (powerConsumerComponent.isPowered())
			{
				spriteComponent.setSpriteLocation(computerSpritesComponent.getRightOn());
			}
			else
			{
				spriteComponent.setSpriteLocation(computerSpritesComponent.getRightOff());
			}
		}
		
	}

}
