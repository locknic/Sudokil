package com.custardgames.sudokil.ui.cli.scripts;

import com.custardgames.sudokil.events.BaseEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.utils.Streams;

public class EventProgram extends Program
{
	private BaseEvent event;
	
	public EventProgram(Streams stream, BaseEvent event)
	{
		super(stream);
		this.event = event;
	}

	@Override
	protected boolean act()
	{
		EventManager.get_instance().broadcast(event);
		
		return true;
	}

}
