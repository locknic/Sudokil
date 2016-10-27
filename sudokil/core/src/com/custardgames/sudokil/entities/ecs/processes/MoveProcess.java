package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.tween.PositionComponentAccessor;
import com.custardgames.sudokil.events.entities.EntityMovedEvent;
import com.custardgames.sudokil.events.entities.map.RequestMoveEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.managers.MapManager;
import com.custardgames.sudokil.managers.UniTweenManager;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Expo;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Sine;

public class MoveProcess extends EntityProcess
{
	private int direction;
	private int distance;

	private PositionComponent position;
	private EntityComponent entityComponent;

	private boolean setTarget;
	private boolean finishedCalculations;
	private boolean finishedAnimations;

	public MoveProcess(Entity entity, int direction, int distance)
	{
		super(entity);
		this.direction = direction;
		this.distance = distance;

		position = entity.getComponent(PositionComponent.class);
		entityComponent = entity.getComponent(EntityComponent.class);

		finishedCalculations = false;
		finishedAnimations = false;
	}

	@Override
	public boolean preProcess()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean process()
	{
		if (!finishedCalculations)
		{
			int dirX = (int) (direction * Math.cos(Math.toRadians(position.getAngle())));
			int dirY = (int) (direction * Math.sin(Math.toRadians(position.getAngle())));

			if (!setTarget)
			{
				float targetX = position.getX() + distance * dirX * MapManager.getTileWidth();
				float targetY = position.getY() + distance * dirY * MapManager.getTileHeight();

				createMoveAccelerationTween(PositionComponentAccessor.X, distance / 2.0f, dirX, targetX);
				createMoveAccelerationTween(PositionComponentAccessor.Y, distance / 2.0f, dirY, targetY);
				
				setTarget = true;
			}

			if (((position.getX() >= position.getExpectedX() && dirX > 0) || (position.getX() <= position.getExpectedX() && dirX < 0))
					|| ((position.getY() >= position.getExpectedY() && dirY > 0) || (position.getY() <= position.getExpectedY() && dirY < 0)))
			{
				position.setX(position.getExpectedX());
				position.setY(position.getExpectedY());
				
				System.out.println("bef" + position.getExpectedX());
				RequestMoveEvent event = (RequestMoveEvent) EventManager.get_instance().broadcastInquiry(new RequestMoveEvent(entity, dirX, dirY));
				System.out.println("aft" + position.getExpectedX());
				if (!event.isAllowedMove())
				{
					if (entityComponent != null && entityComponent.getId() != null)
					{
						sendOutput("WARNING! " + entityComponent.getId() + " path was blocked.");
					}

					createCollisionTween(PositionComponentAccessor.X, dirX);
					createCollisionTween(PositionComponentAccessor.Y, dirY);
					createShakeTween();
					finishedCalculations = true;
				}
				
				distance--;
				if (distance == 0)
				{
					finishedCalculations = true;
				}
			}
		}
		
		EventManager.get_instance().broadcast(new EntityMovedEvent(entity));
		return finishedAnimations;
	}

	@Override
	public void postProcess()
	{
		position.setX(position.getExpectedX());
		position.setY(position.getExpectedY());
	}

	private void createMoveAccelerationTween(int type, float time, float dir, float targetLocation)
	{
		Timeline.createSequence()
				.push(Tween.to(position, type, 1.0f / 4.0f).targetRelative(8 * dir).ease(Sine.IN))
				.push(Tween.to(position, type, time).target(targetLocation - 8 * dir).ease(Linear.INOUT))
				.push(Tween.to(position, type, 1.0f / 2.0f).target(targetLocation).ease(Quad.OUT))
				.setCallback(new TweenCallback()
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

	private void createCollisionTween(int type, int dir)
	{
		UniTweenManager.getTweenManager().killTarget(position, type);
		Timeline.createSequence()
				.push(Tween.to(position, type, 1.0f / 4.0f).targetRelative(8 * dir).ease(Expo.OUT))
				.push(Tween.to(position, type, 1.0f / 2.0f).targetRelative(-8 * dir).ease(Linear.INOUT))
				.start(UniTweenManager.getTweenManager());
	}

	private void createShakeTween()
	{
		Timeline.createSequence().pushPause(1.0f / 16.0f)
				.push(Tween.to(position, PositionComponentAccessor.ANGLE, 1.0f / 32.0f).target(position.getAngle() + 2).ease(Linear.INOUT))
				.push(Tween.to(position, PositionComponentAccessor.ANGLE, 1.0f / 16.0f).target(position.getAngle() - 2).ease(Linear.INOUT))
				.push(Tween.to(position, PositionComponentAccessor.ANGLE, 1.0f / 16.0f).target(position.getAngle() + 2).ease(Linear.INOUT))
				.push(Tween.to(position, PositionComponentAccessor.ANGLE, 1.0f / 16.0f).target(position.getAngle() - 2).ease(Linear.INOUT))
				.push(Tween.to(position, PositionComponentAccessor.ANGLE, 1.0f / 16.0f).target(position.getAngle() + 2).ease(Linear.INOUT))
				.push(Tween.to(position, PositionComponentAccessor.ANGLE, 1.0f / 32.0f).target(position.getAngle()).ease(Linear.INOUT))
				.setCallback(new TweenCallback()
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