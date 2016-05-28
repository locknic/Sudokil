package com.custardgames.sudokil.triggers.conditions;

import java.util.EventListener;
import java.util.UUID;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.custardgames.sudokil.events.DisposeWorldEvent;
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

		EventManager.get_instance().register(DisposeWorldEvent.class, this);
	}

	public void init()
	{
		EventManager.get_instance().register(TimerDoneEvent.class, this);
		owner = UUID.randomUUID();
		
		if(isRunning())
		{
			EventManager.get_instance().broadcast(new TimerRegisterEvent(owner, duration));
		}
	}
	
	@Override
	public void start()
	{
		super.start();
		System.out.println("All part of the plan");
		EventManager.get_instance().broadcast(new TimerRegisterEvent(owner, duration));
	}

	@Override
	public boolean checkConditions()
	{
		return isDone;
	}

	public void handleTimerDoneEvent(TimerDoneEvent event)
	{
		if (isRunning())
		{
			if (owner == event.getOwner())
			{
				isDone = true;
			}
		}
	}

	public void handleDisposeWorld(DisposeWorldEvent event)
	{
		EventManager.get_instance().deregister(DisposeWorldEvent.class, this);
		EventManager.get_instance().deregister(TimerDoneEvent.class, this);
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
