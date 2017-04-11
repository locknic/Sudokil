package com.custardgames.sudokil.events.commandLine;

import com.custardgames.sudokil.utils.Streams;

public class HighlightEvent extends UserInterfaceEvent
{
	private String entity;
	private boolean possibleSelection;

	
	public HighlightEvent(Streams ownerUI, String entity, boolean possibleSelection)
	{
		super(ownerUI);
		this.setEntity(entity);
		this.setPossibleSelection(possibleSelection);
	}

	public boolean isPossibleSelection()
	{
		return possibleSelection;
	}

	public void setPossibleSelection(boolean possibleSelection)
	{
		this.possibleSelection = possibleSelection;
	}

	public String getEntity()
	{
		return entity;
	}

	public void setEntity(String entity)
	{
		this.entity = entity;
	}

}
