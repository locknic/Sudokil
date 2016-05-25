package com.custardgames.sudokil.triggers.conditions;

import java.util.EventListener;
import java.util.UUID;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.custardgames.sudokil.events.TimerDoneEvent;
import com.custardgames.sudokil.events.TimerRegisterEvent;
import com.custardgames.sudokil.managers.EventManager;

public class TimedTriggerCondition extends BaseTriggerCondition implements EventListener, Serializable
{
	private float duration;
	
	private UUID owner;
	
	public TimedTriggerCondition()
	{
		super();
	}
	
	public void init()
	{
		EventManager.get_instance().register(TimerDoneEvent.class, this);
		
		owner = UUID.randomUUID();
		
		EventManager.get_instance().broadcast(new TimerRegisterEvent(owner, duration));
	}
	
	@Override
	public boolean checkConditions()
	{
		return isDone;
	}
	
	public void handleTimerDoneEvent(TimerDoneEvent event)
	{
		if (owner == event.getOwner())
		{
			isDone = true;
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

