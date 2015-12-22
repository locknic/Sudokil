package com.custardgames.sudokil.events;

import com.artemis.Entity;

public class PingEntityEvent extends BaseEvent
{
	private String entityID;
	private Entity entity;

	public PingEntityEvent(String entityID)
	{
		this.entityID = entityID;
	}

	public String getEntityID()
	{
		return entityID;
	}

	public void setEntityID(String entity)
	{
		this.entityID = entity;
	}

	public Entity getEntity()
	{
		return entity;
	}

	public void setEntity(Entity entity)
	{
		this.entity = entity;
	}

}
