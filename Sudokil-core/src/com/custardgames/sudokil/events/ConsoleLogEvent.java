package com.custardgames.sudokil.events;

import java.util.UUID;

public class ConsoleLogEvent
{
	private String message;
	private UUID owner;

	public ConsoleLogEvent(String message, UUID owner)
	{
		setMessage(message);
		setOwnerUI(owner);
	}

	public ConsoleLogEvent(String[] messages, UUID owner)
	{
		String message = "";
		for (int x = 0; x < messages.length; x++)
		{
			message = message + " " + messages[x];
		}
		setMessage(message);

		setOwnerUI(owner);
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public UUID getOwnerUI()
	{
		return owner;
	}

	public void setOwnerUI(UUID owner)
	{
		this.owner = owner;
	}

}
