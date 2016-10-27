package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.BlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.LiftableComponent;
import com.custardgames.sudokil.entities.ecs.components.LifterComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.events.entities.EntityMovedEvent;
import com.custardgames.sudokil.events.entities.UnblockActivityEvent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.events.entities.map.PingCellEvent;
import com.custardgames.sudokil.managers.EventManager;

public class LowerProcess extends EntityProcess
{
	private float targetX, targetY;
	private Entity lifted;

	private LifterComponent lifterComponent;
	private PositionComponent position;

	private PositionComponent liftedPosition;
	private LiftableComponent liftableComponent;
	
	public LowerProcess(Entity entity)
	{
		super(entity);

		lifterComponent = entity.getComponent(LifterComponent.class);
		position = entity.getComponent(PositionComponent.class);
	}

	@Override
	public boolean preProcess()
	{
		if (lifterComponent != null && position != null)
		{
			if (lifterComponent.isLifting())
			{
				lifted = lifterComponent.getLifted();
				if (lifted != null)
				{
					double angle = 0;

					angle = position.getAngle();

					float deltaX = (float) (1 * Math.cos(Math.toRadians(angle)));
					float deltaY = (float) (1 * Math.sin(Math.toRadians(angle)));
					PingCellEvent event = (PingCellEvent) EventManager.get_instance().broadcastInquiry(new PingCellEvent(entity, (int) deltaX, (int) deltaY));
					if (event != null && event instanceof PingCellEvent && event.getEntity() != null && event.getEntity() == entity)
					{
						liftedPosition = lifted.getComponent(PositionComponent.class);

						targetX = event.getxCo() + position.getWidth() / 2 - liftedPosition.getWidth() / 2;
						targetY = event.getyCo() + position.getHeight() / 2 - liftedPosition.getHeight() / 2;
						
						if (event.isFloor())
						{
							Entity destination = event.getCellEntity();
							if (destination == null)
							{
								BlockingComponent blockingComponent = lifted.getComponent(BlockingComponent.class);
								liftableComponent = lifted.getComponent(LiftableComponent.class);
								SpriteComponent spriteComponent = lifted.getComponent(SpriteComponent.class);
								
								if (liftableComponent != null && liftableComponent.isLifted())
								{
									if (blockingComponent != null)
									{
										blockingComponent.setBlocking(true);
									}

									if (spriteComponent != null)
									{
										spriteComponent.setzOrder(spriteComponent.getzOrder() - 10);
									}

									return true;
								}
							}
							else
							{
								sendOutput("ERROR! No space to lower object.");
							}
						}
						else
						{
							sendOutput("ERROR! No space to lower object.");
						}
					}
				}
			}
			else
			{
				sendOutput("ERROR! Not carrying an object.");
			}
		}
		return false;
	}

	@Override
	public boolean process()
	{
		float maxVelocity = lifterComponent.getLiftSpeed();

		float positionX = liftedPosition.getX();
		float positionY = liftedPosition.getY();
		float deltaX = positionX - targetX;
		float deltaY = positionY - targetY;

		if (Math.abs(deltaX) <= maxVelocity && Math.abs(deltaY) <= maxVelocity)
		{
			liftedPosition.setX(targetX);
			liftedPosition.setY(targetY);
			liftedPosition.setExpectedX(targetX);
			liftedPosition.setExpectedY(targetY);
			EventManager.get_instance().broadcast(new AddToMapEvent(lifted));
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

	@Override
	public void postProcess()
	{
		liftableComponent.setLifted(false);
		EventManager.get_instance().broadcast(new UnblockActivityEvent(lifted, liftableComponent.getClass()));
		lifterComponent.setLifting(false);
		lifterComponent.setLifted(null);
	}
}
