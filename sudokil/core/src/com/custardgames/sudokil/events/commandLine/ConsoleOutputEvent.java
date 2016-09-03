package com.custardgames.sudokil.events.commandLine;

import java.util.UUID;

public class ConsoleOutputEvent extends CommandLineEvent
{

	public ConsoleOutputEvent(UUID ownerUI, String text)
	{
		super(ownerUI, text);
	}

	public ConsoleOutputEvent(UUID owner, String[] texts)
	{
		this(owner, parseTexts(texts));
	}

	public static String parseTexts(String[] texts)
	{
		String message = "";
		for (int x = 0; x < texts.length; x++)
		{
			message = message + texts[x] + " ";
		}
		return message;
	}

}
