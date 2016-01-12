package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

public class ConsoleLogEvent extends CommandLineEvent
{

	public ConsoleLogEvent(UUID ownerUI, String text)
	{
		super(ownerUI, text);
	}

	public ConsoleLogEvent(UUID owner, String[] texts)
	{
		this(owner, parseTexts(texts));
	}
	
	public static String parseTexts(String[] texts)
	{
		String message = "";
		for (int x = 0; x < texts.length; x++)
		{
			message = message + " " + texts[x];
		}
		return message;
	}

}
