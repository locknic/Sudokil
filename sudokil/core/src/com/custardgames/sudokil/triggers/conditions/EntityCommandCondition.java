package com.custardgames.sudokil.triggers.conditions;

import java.util.EventListener;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.custardgames.sudokil.events.entities.commands.EntityCommandEvent;
import com.custardgames.sudokil.managers.EventManager;

public class EntityCommandCondition extends BaseTriggerCondition implements EventListener, Serializable
{
	private boolean triggered;
	
	private String entityName;
	private String className;
	
	private Class<?> eventType;
	
	public EntityCommandCondition()
	{
	}
	
	public void init()
	{
		try
		{
			eventType = Class.forName(className);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		EventManager.get_instance().register(eventType, this);
	}
	
	@Override
	public boolean checkConditions()
	{
		return triggered;
	}
	
	public void handleEvent(EntityCommandEvent event)
	{
		if (event.getEntityName().equals(entityName))
		{
			triggered = true;
		}
	}

	@Override
	public void write(Json json)
	{
		json.writeObjectStart();
		json.writeFields(this);
		json.writeObjectEnd();
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		json.readFields(this, jsonData);
		init();
	}

	
}
