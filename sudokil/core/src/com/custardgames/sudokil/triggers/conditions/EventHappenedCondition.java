package com.custardgames.sudokil.triggers.conditions;

import java.util.EventListener;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.custardgames.sudokil.events.BaseEvent;
import com.custardgames.sudokil.events.DisposeWorldEvent;
import com.custardgames.sudokil.managers.EventManager;

public class EventHappenedCondition extends BaseTriggerCondition implements EventListener, Serializable
{
	private boolean triggered;

	private String className;

	private Class<?> eventType;

	public EventHappenedCondition()
	{
		EventManager.get_instance().register(DisposeWorldEvent.class, this);
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

	public void handleDisposeWorld(DisposeWorldEvent event)
	{
		EventManager.get_instance().deregister(DisposeWorldEvent.class, this);
		EventManager.get_instance().deregister(eventType, this);
	}

	public void handleEvent(BaseEvent event)
	{
		if (isRunning())
		{
			if (eventType.isInstance(event))
			{
				triggered = true;
			}
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