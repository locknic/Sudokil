package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.VelocityComponent;

public class TurnProcess extends EntityProcess
{
	private float deltaAngle;
	private float targetAngle;
	private boolean setTarget;

	public TurnProcess(Entity entity, float deltaAngle)
	{
		super(entity);
		this.deltaAngle = deltaAngle;
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
				deltaAngle -= maxTurnVelocity;
			}
			else
			{
				angle -= maxTurnVelocity;
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

			if (position != null)
			{
				position.setAngle(angle);
			}

			return false;
		}
		else
		{
			angle = targetAngle;

			if (position != null)
			{
				position.setAngle(angle);
			}
			return true;
		}

	}

}
