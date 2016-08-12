package com.custardgames.sudokil.events.entities.commands;

public class HighlightEvent extends EntityCommandEvent
{
	private boolean possibleSelection;

	public boolean isPossibleSelection()
	{
		return possibleSelection;
	}

	public void setPossibleSelection(boolean possibleSelection)
	{
		this.possibleSelection = possibleSelection;
	}
}
