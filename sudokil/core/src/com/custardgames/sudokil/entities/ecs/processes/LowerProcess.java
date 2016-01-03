package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.BlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.LiftableComponent;
import com.custardgames.sudokil.entities.ecs.components.LifterComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.map.AddToMapEvent;
import com.custardgames.sudokil.events.map.PingCellEvent;
import com.custardgames.sudokil.managers.EventManager;

public class LowerProcess extends EntityProcess
{

	public LowerProcess(Entity entity)
	{
		super(entity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean process()
	{
		PositionComponent position = entity.getComponent(PositionComponent.class);
		LifterComponent lifterComponent = entity.getComponent(LifterComponent.class);

		if (lifterComponent != null && lifterComponent.isLifting())
		{
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
				if (event.isFloor())
				{
					Entity lifted = event.getCellEntity();
					if (lifted == null)
					{
						BlockingComponent blockingComponent = lifted.getComponent(BlockingComponent.class);
						LiftableComponent liftableComponent = lifted.getComponent(LiftableComponent.class);
						if (liftableComponent != null && liftableComponent.isLifted())
						{
							EventManager.get_instance().broadcast(new AddToMapEvent(lifted));
							liftableComponent.setLifted(false);
							blockingComponent.setBlocking(true);
							lifterComponent.setLifting(false);
							lifterComponent.setLifted(null);
						}
					}
				}
			}

		}
		return true;
	}

}
