package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.BlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.LiftableComponent;
import com.custardgames.sudokil.entities.ecs.components.LifterComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.events.entities.BlockActivityEvent;
import com.custardgames.sudokil.events.entities.EntityMovedEvent;
import com.custardgames.sudokil.events.entities.map.PingCellEvent;
import com.custardgames.sudokil.events.entities.map.RemoveFromMapEvent;
import com.custardgames.sudokil.managers.EventManager;

public class LiftProcess extends EntityProcess
{
	private float targetX, targetY;
	private boolean setTarget;
	private Entity lifted;

	public LiftProcess(Entity entity)
	{
		super(entity);
		setTarget = false;
	}

	@Override
	public boolean process()
	{
		PositionComponent position = entity.getComponent(PositionComponent.class);
		LifterComponent lifterComponent = entity.getComponent(LifterComponent.class);

		if (lifterComponent != null)
		{
			float positionX = 0;
			float positionY = 0;
			double angle = 0;
			float maxVelocity = lifterComponent.getLiftSpeed();

			if (position != null)
			{
				angle = position.getAngle();
			}
			else
			{
				return true;
			}

			if (!setTarget)
			{
				if (!lifterComponent.isLifting())
				{
					positionX = (float) (1 * Math.cos(Math.toRadians(angle)));
					positionY = (float) (1 * Math.sin(Math.toRadians(angle)));
					PingCellEvent event = (PingCellEvent) EventManager.get_instance()
							.broadcastInquiry(new PingCellEvent(entity, (int) positionX, (int) positionY));
					if (event != null && event instanceof PingCellEvent && event.getEntity() != null && event.getEntity() == entity)
					{
						lifted = event.getCellEntity();
						if (lifted != null)
						{
							PositionComponent liftedPosition = lifted.getComponent(PositionComponent.class);
							LiftableComponent liftableComponent = lifted.getComponent(LiftableComponent.class);
							BlockingComponent blockingComponent = lifted.getComponent(BlockingComponent.class);
							SpriteComponent spriteComponent = lifted.getComponent(SpriteComponent.class);
							if (liftableComponent != null && !liftableComponent.isLifted())
							{
								EventManager.get_instance().broadcast(new RemoveFromMapEvent(lifted));
								liftableComponent.setLifted(true);
								if (blockingComponent != null)
								{
									blockingComponent.setBlocking(false);
								}
								lifterComponent.setLifting(true);
								lifterComponent.setLifted(lifted);
								EventManager.get_instance().broadcast(new BlockActivityEvent(lifted, liftableComponent.getClass()));
								targetX = position.getX() + position.getWidth() / 2 - liftedPosition.getWidth() / 2;
								targetY = position.getY() + position.getHeight() / 2 - liftedPosition.getHeight() / 2;
								setTarget = true;
								
								if (spriteComponent != null)
								{
									spriteComponent.setzOrder(spriteComponent.getzOrder() + 10);
								}
								return false;
							}
							else
							{
								sendOutput("ERROR! Object is not liftable.");
							}
						}
						else
						{
							sendOutput("ERROR! No object to lift.");
						}
					}
				}
				else
				{
					sendOutput("ERROR! Entity is already lifting something.");
				}
				return true;
			}
			PositionComponent liftedPosition = lifted.getComponent(PositionComponent.class);
			if (liftedPosition != null)
			{
				positionX = liftedPosition.getX();
				positionY = liftedPosition.getY();
				float deltaX = positionX - targetX;
				float deltaY = positionY - targetY;

				if (Math.abs(deltaX) <= maxVelocity && Math.abs(deltaY) <= maxVelocity)
				{
					liftedPosition.setX(targetX);
					liftedPosition.setY(targetY);
					EventManager.get_instance().broadcast(new EntityMovedEvent(lifted));
					return true;
				}

				if (deltaX != 0)
				{
					deltaX = -maxVelocity * (deltaX / Math.abs(deltaX));
				}
				if (deltaY != 0)
				{
					deltaY = -maxVelocity * (deltaY / Math.abs(deltaY));
				}
				liftedPosition.setX(positionX + deltaX);
				liftedPosition.setY(positionY + deltaY);
				
				EventManager.get_instance().broadcast(new EntityMovedEvent(lifted));
				return false;
			}
		}
		return true;
	}

}
