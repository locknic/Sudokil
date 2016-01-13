package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.VelocityComponent;
import com.custardgames.sudokil.events.entities.EntityMovedEvent;
import com.custardgames.sudokil.events.entities.map.RequestMoveEvent;
import com.custardgames.sudokil.managers.EventManager;

public class MoveProcess extends EntityProcess
{
	private int direction;
	private float deltaX, deltaY;
	private float targetX, targetY;
	private boolean setTarget;

	public MoveProcess(Entity entity, int direction)
	{
		super(entity);
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
					&& event.getEntity() == entity)
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
			
			deltaX = ((float) ((maxVelocity * Math.cos(travelDirection))));
			deltaY = ((float) ((maxVelocity * Math.sin(travelDirection))));
			positionX += deltaX;
			positionY += deltaY;

			position.setX(positionX);
			position.setY(positionY);
			
			EventManager.get_instance().broadcast(new EntityMovedEvent(entity, deltaX, deltaY));

			return false;
		}
		else
		{
			EventManager.get_instance().broadcast(new EntityMovedEvent(entity, targetX - positionX, targetY - positionY));

			positionX = targetX;
			positionY = targetY;

			position.setX(positionX);
			position.setY(positionY);

			return true;
		}
	}

}
