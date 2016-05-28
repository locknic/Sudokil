package com.custardgames.sudokil.managers;

import java.util.EventListener;
import java.util.Iterator;

import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.events.TimerDoneEvent;
import com.custardgames.sudokil.events.TimerRegisterEvent;
import com.custardgames.sudokil.utils.TimeNode;

public class TimerManager implements EventListener
{
	private Array<TimeNode> timeNodes;

	public TimerManager()
	{
		EventManager.get_instance().register(TimerRegisterEvent.class, this);
		timeNodes = new Array<TimeNode>();
	}

	public void dispose()
	{
		EventManager.get_instance().deregister(TimerRegisterEvent.class, this);
		timeNodes.clear();
	}

	public void update(float time)
	{
		Iterator<TimeNode> iterator = timeNodes.iterator();
		while (iterator.hasNext())
		{
			TimeNode timeNode = iterator.next();
			timeNode.process(time);
			if (timeNode.isFinished())
			{
				EventManager.get_instance().broadcast(new TimerDoneEvent(timeNode.getOwner()));
				iterator.remove();
			}
		}
	}

	public void handleTimerRegisterEvent(TimerRegisterEvent event)
	{
		timeNodes.add(new TimeNode(event.getOwner(), event.getDuration()));
	}
}
