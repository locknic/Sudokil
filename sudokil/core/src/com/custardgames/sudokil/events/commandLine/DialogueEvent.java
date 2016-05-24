package com.custardgames.sudokil.events.commandLine;

import com.custardgames.sudokil.events.BaseEvent;

public class DialogueEvent extends BaseEvent
{
	private String dialogue;

	public DialogueEvent(String dialogue)
	{
		this.dialogue = dialogue;
	}

	public String getDialogue()
	{
		return dialogue;
	}
}
