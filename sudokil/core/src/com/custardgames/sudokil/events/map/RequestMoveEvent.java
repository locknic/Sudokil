package com.custardgames.sudokil.events.map;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.events.BaseEvent;

public class RequestMoveEvent extends BaseEvent
{
	private Entity entity;
	private int xDir, yDir;
	private boolean allowedMove;

	public RequestMoveEvent(Entity entity, int xDir, int yDir)
	{
		super(entity.getComponent(EntityComponent.class).getId());
		this.setEntity(entity);
		this.setxDir(xDir);
		this.setyDir(yDir);
	}

	public Entity getEntity()
	{
		return entity;
	}

	public void setEntity(Entity entity)
	{
		this.entity = entity;
	}

	public int getxDir()
	{
		return xDir;
	}

	public void setxDir(int xDir)
	{
		this.xDir = xDir;
	}

	public int getyDir()
	{
		return yDir;
	}

	public void setyDir(int yDir)
	{
		this.yDir = yDir;
	}

	public boolean isAllowedMove()
	{
		return allowedMove;
	}

	public void setAllowedMove(boolean allowedMove)
	{
		this.allowedMove = allowedMove;
	}

}
