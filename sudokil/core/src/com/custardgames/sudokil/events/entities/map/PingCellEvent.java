package com.custardgames.sudokil.events.entities.map;

import com.artemis.Entity;
import com.custardgames.sudokil.events.entities.EntityEvent;

public class PingCellEvent extends EntityEvent
{
	private int xDir, yDir;

	private Entity cellEntity;
	private float xCo, yCo;
	private boolean floor;

	public PingCellEvent(Entity entity, int xDir, int yDir)
	{
		super(entity);
		setxDir(xDir);
		setyDir(yDir);
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

	public Entity getCellEntity()
	{
		return cellEntity;
	}

	public void setCellEntity(Entity cellEntity)
	{
		this.cellEntity = cellEntity;
	}

	public boolean isFloor()
	{
		return floor;
	}

	public void setFloor(boolean floor)
	{
		this.floor = floor;
	}

	public float getyCo()
	{
		return yCo;
	}

	public void setyCo(float yCo)
	{
		this.yCo = yCo;
	}

	public float getxCo()
	{
		return xCo;
	}

	public void setxCo(float xCo)
	{
		this.xCo = xCo;
	}

}
