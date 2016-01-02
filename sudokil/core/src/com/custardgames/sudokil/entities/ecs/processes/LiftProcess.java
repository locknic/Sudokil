package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.BlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.LiftableComponent;
import com.custardgames.sudokil.entities.ecs.components.LifterComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.map.PingCellEvent;
import com.custardgames.sudokil.events.map.RemoveFromMapEvent;
import com.custardgames.sudokil.managers.EventManager;

public class LiftProcess extends EntityProcess
{

	public LiftProcess(Entity entity)
	{
		super(entity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean process()
	{
		PositionComponent position = entity.getComponent(PositionComponent.class);
		LifterComponent lifterComponent = entity.getComponent(LifterComponent.class);
		
		if (lifterComponent != null && !lifterComponent.isLifting())
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
			PingCellEvent event = (PingCellEvent) EventManager.get_instance()
					.broadcastInquiry(new PingCellEvent(entity, (int) deltaX, (int) deltaY));
			if (event != null && event instanceof PingCellEvent
					&& event.getOwner() != null && event.getOwner().equals(entity.getComponent(EntityComponent.class).getId()))
			{
				Entity lifted = event.getCellEntity();
				if (lifted != null)
				{
					LiftableComponent liftableComponent = lifted.getComponent(LiftableComponent.class);
					BlockingComponent blockingComponent = lifted.getComponent(BlockingComponent.class);
					if (liftableComponent != null && !liftableComponent.isLifted())
					{
						EventManager.get_instance().broadcast(new RemoveFromMapEvent(lifted));
						liftableComponent.setLifted(true);
						blockingComponent.setBlocking(false);
						lifterComponent.setLifting(true);
						lifterComponent.setLifted(lifted);
					}
				}
			}

		}
		return true;
	}

}
