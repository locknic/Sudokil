package com.custardgames.sudokil.ui.cli;

import java.util.UUID;

import com.custardgames.sudokil.events.commandLine.CommandEvent;
import com.custardgames.sudokil.managers.EventManager;

public class TextFileCLI extends FileCLI
{
	private String content;
	
	public TextFileCLI()
	{
		this.readPerm = true;
	}
	
	public TextFileCLI(String content)
	{
		this.setContent(content);
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
	
	public void appendContent(String additions)
	{
		this.content += additions;
	}
	
	@Override
	public void run(UUID ownerUI, String[] args)
	{
		if (this.getName().endsWith(".sh"))
		{
			String[] commands = content.split("\n");
			for (String command : commands)
			{
				EventManager.get_instance().broadcast(new CommandEvent(ownerUI, command));
			}
		}
		else
		{
			super.run(ownerUI, args);
		}
	}
	
	@Override
	public String read()
	{
		return getContent();
	}
}
