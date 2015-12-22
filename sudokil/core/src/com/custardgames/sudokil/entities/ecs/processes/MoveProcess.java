package com.custardgames.sudokil.entities.ecs.processes;

import java.util.EventListener;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.VelocityComponent;
import com.custardgames.sudokil.events.map.RequestMoveEvent;
import com.custardgames.sudokil.managers.EventManager;

public class MoveProcess extends EntityProcess implements EventListener
{
	private int direction;
	private float deltaX, deltaY;
	private float targetX, targetY;
	private boolean setTarget;

	public MoveProcess(Entity e, int direction)
	{
		super(e);
		this.direction = direction;

	}

	@Override
	public boolean process()
	{
		PositionComponent position = entity.getComponent(PositionComponent.class);
		VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

		float positionX = 0;
		float positionY = 0;
		double angle = 0;
		float maxVelocity = 4;

		if (position != null)
		{
			positionX = position.getX();
			positionY = position.getY();
			angle = position.getAngle();
		}
		else
		{
			return true;
		}

		if (velocity != null)
		{
			maxVelocity = velocity.getMaxVelocity();
		}

		if (!setTarget)
		{
			deltaX = (float) (direction * Math.cos(Math.toRadians(angle)));
			deltaY = (float) (direction * Math.sin(Math.toRadians(angle)));
			targetX = positionX;
			targetY = positionY;
			RequestMoveEvent event = (RequestMoveEvent) EventManager.get_instance()
					.broadcastInquiry(new RequestMoveEvent(entity, (int) deltaX, (int) deltaY));

			if (event != null && event instanceof RequestMoveEvent
					&& event.getOwner().equals(entity.getComponent(EntityComponent.class).getId()))
			{
				if (event.isAllowedMove())
				{
					targetX += event.getxDir();
					targetY += event.getyDir();

				}
				else
				{
					return true;
				}
			}
			else
			{
				return true;
			}

			setTarget = true;
		}

		deltaX = targetX - positionX;
		deltaY = targetY - positionY;

		if (Math.abs(deltaX) + Math.abs(deltaY) > maxVelocity)
		{
			double travelDirection = Math.atan2(deltaY, deltaX);

			positionX = ((float) (positionX + (maxVelocity * Math.cos(travelDirection))));
			positionY = ((float) (positionY + (maxVelocity * Math.sin(travelDirection))));

			position.setX(positionX);
			position.setY(positionY);

			return false;
		}
		else
		{

			positionX = targetX;
			positionY = targetY;

			position.setX(positionX);
			position.setY(positionY);

			return true;
		}
	}

}
