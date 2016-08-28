package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.BlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.LiftableComponent;
import com.custardgames.sudokil.entities.ecs.components.LifterComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.events.entities.BlockActivityEvent;
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
								targetX = position.getX();
								targetY = position.getY();
								setTarget = true;
								
								if (spriteComponent != null)
								{
									spriteComponent.setzOrder(spriteComponent.getzOrder() + 10);
								}
								return false;
							}
						}
					}
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
					return true;
				}

				if (deltaX != 0)
					positionX = ((float) (positionX + (-maxVelocity * (deltaX / Math.abs(deltaX)))));
				if (deltaY != 0)
					positionY = ((float) (positionY + (-maxVelocity * (deltaY / Math.abs(deltaY)))));

				liftedPosition.setX(positionX);
				liftedPosition.setY(positionY);
				return false;
			}
		}
		return true;
	}

}
