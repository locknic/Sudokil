package com.custardgames.sudokil.ui.cli.scripts;

import java.util.EventListener;

import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.events.commandLine.CommandLineEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.utils.Streams;

public abstract class Program implements EventListener
{
	private Streams stream;
	private Array<String> lastInput;
	protected boolean usedInput;

	public Program(Streams stream)
	{
		EventManager.get_instance().register(CommandLineEvent.class, this);

		this.stream = stream;
		lastInput = new Array<String>();
	}

	public final boolean run()
	{
		boolean act = act();

		if (act)
		{
			finish();
		}

		return act;
	}

	protected abstract boolean act();

	public void finish()
	{
		EventManager.get_instance().deregister(CommandLineEvent.class, this);
	}

	public String requestInput()
	{
		String output = null;
		if (lastInput.size > 0)
		{
			output = lastInput.first();
			lastInput.removeIndex(0);
			usedInput = true;
		}
		else
		{
			usedInput = false;
		}
		return output;
	}

	public void handleCommandLine(CommandLineEvent event)
	{
		if (event.getOwnerUI().getOutput().equals(stream.getInput()))
		{
			lastInput.add(event.getText());
		}
	}
}
