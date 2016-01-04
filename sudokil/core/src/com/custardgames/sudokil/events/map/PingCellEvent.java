package com.custardgames.sudokil.events.map;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.events.BaseEvent;

public class PingCellEvent extends BaseEvent
{
	private Entity ownerEntity;
	private int xDir, yDir;
	private Entity cellEntity;
	private float xCo, yCo;
	private boolean floor;

	public PingCellEvent(Entity entity, int xDir, int yDir)
	{
		super(entity.getComponent(EntityComponent.class).getId());
		setOwnerEntity(entity);
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
