package com.custardgames.sudokil.entities.ecs.systems.entities.robot;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.entities.ecs.components.entities.robot.RobotSpritesComponent;

public class RobotSpriteSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<PositionComponent> positionComponents;
	private ComponentMapper<SpriteComponent> spriteComponents;
	private ComponentMapper<RobotSpritesComponent> robotSpritesComponents;
	
	@SuppressWarnings("unchecked")
	public RobotSpriteSystem()
	{
		super(Aspect.all(PositionComponent.class, SpriteComponent.class, RobotSpritesComponent.class));

	}

	@Override
	protected void process(Entity e)
	{
		PositionComponent positionComponent = positionComponents.get(e);
		SpriteComponent spriteComponent = spriteComponents.get(e);
		RobotSpritesComponent robotSpritesComponent = robotSpritesComponents.get(e);
		
		float angle = positionComponent.getNormalAngle();
		
		if (angle >= 225 && angle < 315)
		{
			spriteComponent.setSpriteLocation(robotSpritesComponent.getRobotDown());
		}
		else if (angle >= 135 && angle < 315)
		{
			spriteComponent.setSpriteLocation(robotSpritesComponent.getRobotLeft());
		}
		else if (angle >= 45 && angle < 315)
		{
			spriteComponent.setSpriteLocation(robotSpritesComponent.getRobotUp());
		}
		else
		{
			spriteComponent.setSpriteLocation(robotSpritesComponent.getRobotRight());
		}
		
	}

}
