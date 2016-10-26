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
	private boolean setTarget;
	private Entity lifted;

	public LowerProcess(Entity entity)
	{
		super(entity);
		setTarget = false;
	}

	@Override
	public boolean process()
	{
		LifterComponent lifterComponent = entity.getComponent(LifterComponent.class);

		if (lifterComponent != null)
		{
			if (!setTarget)
			{
				if (lifterComponent.isLifting())
				{
					lifted = lifterComponent.getLifted();
					if (lifted != null)
					{
						PositionComponent position = entity.getComponent(PositionComponent.class);
						double angle = 0;

						if (position != null)
						{
							angle = position.getAngle();
						}
						else
						{
							return true;
						}

						float deltaX = (float) (1 * Math.cos(Math.toRadians(angle)));
						float deltaY = (float) (1 * Math.sin(Math.toRadians(angle)));
						PingCellEvent event = (PingCellEvent) EventManager.get_instance()
								.broadcastInquiry(new PingCellEvent(entity, (int) deltaX, (int) deltaY));
						if (event != null && event instanceof PingCellEvent && event.getEntity() != null && event.getEntity() == entity)
						{
							PositionComponent liftedPosition = lifted.getComponent(PositionComponent.class);

							targetX = event.getxCo() + position.getWidth() / 2 - liftedPosition.getWidth() / 2;
							targetY = event.getyCo()+ position.getHeight() / 2 - liftedPosition.getHeight() / 2;
							if (event.isFloor())
							{
								Entity destination = event.getCellEntity();
								if (destination == null)
								{
									BlockingComponent blockingComponent = lifted.getComponent(BlockingComponent.class);
									LiftableComponent liftableComponent = lifted.getComponent(LiftableComponent.class);
									SpriteComponent spriteComponent = lifted.getComponent(SpriteComponent.class);
									if (liftableComponent != null && liftableComponent.isLifted())
									{
										liftableComponent.setLifted(false);
										if (blockingComponent != null)
										{
											blockingComponent.setBlocking(true);
										}
										EventManager.get_instance().broadcast(new UnblockActivityEvent(lifted, liftableComponent.getClass()));
										lifterComponent.setLifting(false);
										lifterComponent.setLifted(null);
										setTarget = true;
										
										if (spriteComponent != null)
										{
											spriteComponent.setzOrder(spriteComponent.getzOrder() - 10);
										}
										return false;
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
				return true;
			}
			float maxVelocity = lifterComponent.getLiftSpeed();
			PositionComponent liftedPosition = lifted.getComponent(PositionComponent.class);

			if (liftedPosition != null)
			{
				float positionX = liftedPosition.getX();
				float positionY = liftedPosition.getY();
				float deltaX = positionX - targetX;
				float deltaY = positionY - targetY;

				if (Math.abs(deltaX) <= maxVelocity && Math.abs(deltaY) <= maxVelocity)
				{
					liftedPosition.setX(targetX);
					liftedPosition.setY(targetY);
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
		}
		return true;
	}
}
