package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.BlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.LiftableComponent;
import com.custardgames.sudokil.entities.ecs.components.LifterComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.entities.UnblockActivityEvent;
import com.custardgames.sudokil.events.map.AddToMapEvent;
import com.custardgames.sudokil.events.map.PingCellEvent;
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
						PingCellEvent event = (PingCellEvent) EventManager.get_instance().broadcastInquiry(new PingCellEvent(entity, (int) deltaX, (int) deltaY));
						if (event != null && event instanceof PingCellEvent && event.getOwner() != null && event.getOwner().equals(entity.getComponent(EntityComponent.class).getId()))
						{
							targetX = event.getxCo();
							targetY = event.getyCo();
							if (event.isFloor())
							{
								Entity destination = event.getCellEntity();
								if (destination == null)
								{
									BlockingComponent blockingComponent = lifted.getComponent(BlockingComponent.class);
									LiftableComponent liftableComponent = lifted.getComponent(LiftableComponent.class);
									if (liftableComponent != null && liftableComponent.isLifted())
									{
										liftableComponent.setLifted(false);
										if (blockingComponent != null)
										{
											blockingComponent.setBlocking(true);
										}
										EventManager.get_instance().broadcast(new UnblockActivityEvent(lifted, liftableComponent));
										lifterComponent.setLifting(false);
										lifterComponent.setLifted(null);
										setTarget = true;
										return false;
									}
								}
							}
						}
					}
				}
				return true;
			}
			PositionComponent liftedPosition = lifted.getComponent(PositionComponent.class);
			float maxVelocity = lifterComponent.getLiftSpeed();
			
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
