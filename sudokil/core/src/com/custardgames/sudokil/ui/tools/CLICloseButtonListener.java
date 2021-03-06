package com.custardgames.sudokil.ui.tools;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.custardgames.sudokil.events.commandLine.CloseCommandLineWindowEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.ui.windows.CommandLineInterface;
import com.custardgames.sudokil.utils.Streams;

public class CLICloseButtonListener extends CloseButtonListener
{
	private CommandLineInterface cli;

	public CLICloseButtonListener(CommandLineInterface cli, Actor actor)
	{
		super(actor);

		this.cli = cli;
	}

	@Override
	public void clicked(InputEvent event, float x, float y)
	{
		super.clicked(event, x, y);
		EventManager.get_instance().broadcast(new CloseCommandLineWindowEvent(new Streams(cli.getUUID()), cli.getAllUUIDs()));
	}

}
