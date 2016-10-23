package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.VelocityComponent;
import com.custardgames.sudokil.events.entities.EntityTurnedEvent;
import com.custardgames.sudokil.managers.EventManager;

public class TurnProcess extends EntityProcess
{
	private float dirAngle;
	private float deltaAngle;
	private float targetAngle;
	private boolean setTarget;
	private int times;

	public TurnProcess(Entity entity, float dirAngle, int times)
	{
		super(entity);
		this.dirAngle = dirAngle;
		this.times = times;
	}

	@Override
	public boolean process()
	{
		PositionComponent position = entity.getComponent(PositionComponent.class);
		VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

		float angle = 0;
		float maxTurnVelocity = (float) 1.5;

		if (position != null)
		{
			angle = position.getAngle();
		}
		else
		{
			return true;
		}

		if (velocity != null)
		{
			maxTurnVelocity = (float) velocity.getMaxTurnVelocity();
		}

		if (!setTarget)
		{
			deltaAngle = dirAngle;
			targetAngle = angle + deltaAngle;

			if (targetAngle >= 360)
			{
				targetAngle -= 360;
			}
			else if (targetAngle < 0)
			{
				targetAngle += 360;
			}

			setTarget = true;
		}

		if (Math.abs(deltaAngle) > maxTurnVelocity)
		{
			if (deltaAngle > 0)
			{
				angle += maxTurnVelocity;
				EventManager.get_instance().broadcast(new EntityTurnedEvent(entity, maxTurnVelocity));
				deltaAngle -= maxTurnVelocity;
			}
			else
			{
				angle -= maxTurnVelocity;
				EventManager.get_instance().broadcast(new EntityTurnedEvent(entity, -maxTurnVelocity));
				deltaAngle += maxTurnVelocity;
			}

			if (angle >= 360)
			{
				angle -= 360;
			}
			else if (angle < 0)
			{
				angle += 360;
			}
			position.setAngle(angle);

			return false;
		}
		else
		{
			EventManager.get_instance().broadcast(new EntityTurnedEvent(entity, targetAngle - angle));
			angle = targetAngle;

			position.setAngle(angle);
			
			times--;
			if (times > 0)
			{
				setTarget = false;
				return false;
			}
			else
			{
				return true;
			}
		}

	}

}
