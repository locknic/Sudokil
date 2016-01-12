package com.custardgames.sudokil.events.entities.commands;

import java.util.UUID;

import com.custardgames.sudokil.events.BaseEvent;

public abstract class EntityCommandEvent extends BaseEvent
{
	private UUID ownerUI;
	private String entityName;
	private String[] args;

	public String getEntityName()
	{
		return entityName;
	}

	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}

	public String[] getArgs()
	{
		return args;
	}

	public void setArgs(String[] args)
	{
		this.args = args;
	}

	public UUID getOwnerUI()
	{
		return ownerUI;
	}

	public void setOwnerUI(UUID ownerUI)
	{
		this.ownerUI = ownerUI;
	}

}
