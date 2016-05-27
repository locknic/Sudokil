package com.custardgames.sudokil.events.ui;

import com.custardgames.sudokil.events.BaseEvent;

public class ToggleDialogueWindowEvent extends BaseEvent
{
	private boolean open;
	
	public ToggleDialogueWindowEvent()
	{
		this.open = false;
	}
	
	public ToggleDialogueWindowEvent(boolean open)
	{
		this.open = open;
	}
	
	public boolean isOpen()
	{
		return open;
	}
}
