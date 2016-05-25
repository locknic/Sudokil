package com.custardgames.sudokil.triggers.reactions;

import com.custardgames.sudokil.events.commandLine.DialogueEvent;
import com.custardgames.sudokil.managers.EventManager;

public class PrintDialogueReaction extends BaseTriggerReaction
{
	private String dialogueMessage;
	
	@Override
	public void initialiseReaction()
	{
		EventManager.get_instance().broadcast(new DialogueEvent(dialogueMessage));
	}

}
