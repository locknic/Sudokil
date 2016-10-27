package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityStateComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityStateComponent.EntityStates;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.VelocityComponent;
import com.custardgames.sudokil.entities.ecs.components.tween.PositionComponentAccessor;
import com.custardgames.sudokil.events.entities.EntityTurnedEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.managers.UniTweenManager;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Sine;

public class TurnProcess extends EntityProcess
{
	private float dirAngle;
	private int times;

	private PositionComponent position;
	private VelocityComponent velocity;
	private EntityStateComponent entityState;

	private boolean finishedAnimations;

	public TurnProcess(Entity entity, float dirAngle, int times)
	{
		super(entity);
		this.dirAngle = dirAngle;
		this.times = times;

		position = entity.getComponent(PositionComponent.class);
		velocity = entity.getComponent(VelocityComponent.class);
		entityState = entity.getComponent(EntityStateComponent.class);
	}

	@Override
	public boolean preProcess()
	{
		boolean canContinue = (position != null && velocity != null);

		if (canContinue)
		{
			createTurnAccelerationTween(times * 1.0f, dirAngle, position.getAngle() + 90 * dirAngle * times);
			
			if (entityState != null)
			{
				entityState.pushState(EntityStates.TURNING);
			}
		}

		return canContinue;
	}

	@Override
	public boolean process()
	{
		EventManager.get_instance().broadcast(new EntityTurnedEvent(entity));
		return finishedAnimations;
	}

	@Override
	public void postProcess()
	{
		if (entityState != null)
		{
			entityState.popState(EntityStates.TURNING);
		}
	}

	private void createTurnAccelerationTween(float time, float dir, float targetAngle)
	{
		Timeline.createSequence().push(Tween.to(position, PositionComponentAccessor.ANGLE, 1.0f / 4.0f).targetRelative(15 * dir).ease(Sine.IN))
				.push(Tween.to(position, PositionComponentAccessor.ANGLE, time).target(targetAngle - 10 * dir).ease(Linear.INOUT))
				.push(Tween.to(position, PositionComponentAccessor.ANGLE, 1.0f / 2.0f).target(targetAngle).ease(Quad.OUT)).setCallback(new TweenCallback()
				{
					@Override
					public void onEvent(int type, BaseTween<?> source)
					{
						if (type == COMPLETE)
						{
							finishedAnimations = true;
						}
					}
				}).start(UniTweenManager.getTweenManager());
	}
}
