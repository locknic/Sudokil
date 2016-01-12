package com.custardgames.sudokil.events.entities.map;

import com.artemis.Entity;
import com.custardgames.sudokil.events.entities.EntityEvent;

public class RequestMoveEvent extends EntityEvent
{
	private int xDir, yDir;
	private boolean allowedMove;

	public RequestMoveEvent(Entity entity, int xDir, int yDir)
	{
		super(entity);
		this.setxDir(xDir);
		this.setyDir(yDir);
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
