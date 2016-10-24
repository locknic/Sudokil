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

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Sine;

public class MoveProcess extends EntityProcess
{
	private int direction;
	private int distance;

	private float targetX, targetY;
	private boolean setTarget;

	private PositionComponent position;
	private EntityComponent entityComponent;

	public MoveProcess(Entity entity, int direction, int distance)
	{
		super(entity);
		this.direction = direction;
		this.distance = distance;

		position = entity.getComponent(PositionComponent.class);
		entityComponent = entity.getComponent(EntityComponent.class);
	}
	
	private void createAccelerationTween(int type, float time, float dir, float targetLocation)
	{
		Timeline.createSequence()
		.push(Tween.to(position, type, 1.0f / 4.0f).targetRelative(8 * dir).ease(Sine.IN))
		.push(Tween.to(position, type, time).target(targetLocation - 8 * dir).ease(Linear.INOUT))
		.push(Tween.to(position, type, 1.0f / 2.0f).target(targetLocation).ease(Quad.OUT))
		.start(UniTweenManager.getTweenManager());
	}
	
	private void createCollisionTween(int type, int dir)
	{
		UniTweenManager.getTweenManager().killTarget(position, type);
//		Timeline.createSequence()
//		.push(Tween.to(position, type, 1.0f / 16.0f).targetRelative(2 * dir).ease(Expo.OUT))
//		.push(Tween.to(position, type, 1.0f / 8.0f).targetRelative(-3 * dir).ease(Linear.INOUT))
//		.push(Tween.to(position, type, 1.0f / 16.0f).targetRelative(1 * dir).ease(Linear.INOUT))
//		.start(UniTweenManager.getTweenManager());
	}
	
	private void createShakeTween()
	{
		Timeline.createSequence()
		.push(Tween.to(position, PositionComponentAccessor.ANGLE, 1.0f / 32.0f).target(position.getAngle() + 2).ease(Linear.INOUT))
		.push(Tween.to(position, PositionComponentAccessor.ANGLE, 1.0f / 16.0f).target(position.getAngle() + -4).ease(Linear.INOUT))
		.push(Tween.to(position, PositionComponentAccessor.ANGLE, 1.0f / 16.0f).target(position.getAngle() + 4).ease(Linear.INOUT))
		.push(Tween.to(position, PositionComponentAccessor.ANGLE, 1.0f / 32.0f).target(position.getAngle() + -2).ease(Linear.INOUT))
		.start(UniTweenManager.getTweenManager());
	}
	
	@Override
	public boolean process()
	{
		int dirX = (int) (direction * Math.cos(Math.toRadians(position.getAngle())));
		int dirY = (int) (direction * Math.sin(Math.toRadians(position.getAngle())));

		if (!setTarget)
		{
			targetX = position.getX() + distance * dirX * MapManager.getTileWidth();
			targetY = position.getY() + distance * dirY * MapManager.getTileHeight();

			createAccelerationTween(PositionComponentAccessor.X, distance / 2.0f, dirX, targetX);
			createAccelerationTween(PositionComponentAccessor.Y, distance / 2.0f, dirY, targetY);
			setTarget = true;
		}

		if (((position.getX() >= position.getExpectedX() && dirX > 0) || (position.getX() <= position.getExpectedX() && dirX < 0))
				|| ((position.getY() >= position.getExpectedY() && dirY > 0) || (position.getY() <= position.getExpectedY() && dirY < 0)))
		{
			EventManager.get_instance().broadcast(new EntityMovedEvent(entity, dirX * MapManager.getTileWidth(), dirY * MapManager.getTileHeight()));
			position.setX(position.getExpectedX());
			position.setY(position.getExpectedY());

			if (distance == 0)
			{
				return true;
			}

			distance--;

			RequestMoveEvent event = (RequestMoveEvent) EventManager.get_instance().broadcastInquiry(new RequestMoveEvent(entity, dirX, dirY));
			if (!event.isAllowedMove())
			{
				if (entityComponent != null && entityComponent.getId() != null)
				{
					sendOutput("WARNING! " + entityComponent.getId() + " path was blocked.");
				}

				createCollisionTween(PositionComponentAccessor.X, dirX);
				createCollisionTween(PositionComponentAccessor.Y, dirY);
				createShakeTween();
				return true;
			}
		}

		return false;
	}
}