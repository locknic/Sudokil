package com.custardgames.sudokil.events.map;

import com.artemis.Entity;

public class PingCellEvent
{
	private Entity ownerEntity;
	private int xDir, yDir;
	private Entity cellEntity;

	public PingCellEvent(Entity ownerEntity, int xDir, int yDir)
	{
		setOwnerEntity(ownerEntity);
		setxDir(xDir);
		setyDir(yDir);
	}

	public Entity getOwnerEntity()
	{
		return ownerEntity;
	}

	public void setOwnerEntity(Entity ownerEntity)
	{
		this.ownerEntity = ownerEntity;
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

}
