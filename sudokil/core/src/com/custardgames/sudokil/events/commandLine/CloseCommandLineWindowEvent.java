package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.utils.Streams;

public class CloseCommandLineWindowEvent extends UserInterfaceEvent
{
	private Array<UUID> uuids;
	
	public CloseCommandLineWindowEvent(Streams ownerUI, Array<UUID> uuids)
	{
		super(ownerUI);
		this.setUuids(uuids);
	}

	public Array<UUID> getUuids()
	{
		return uuids;
	}

	public void setUuids(Array<UUID> uuids)
	{
		this.uuids = uuids;
	}
}
