package com.custardgames.sudokil.ui;

import java.util.EventListener;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.events.commandLine.CloseCommandLineWindowEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleConnectEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.managers.FileSystemManager;
import com.custardgames.sudokil.managers.InputManager;

public class UserInterface extends Stage implements EventListener
{
	private String root;
	private Array<CommandLineInterface> windows;
	private CommandLineInterface previousFocus;
	private int maxWindows;
	private Button newTerminalWindow;

	private FileSystemManager fileSystemManager;

	public UserInterface()
	{
		InputManager.get_instance().addProcessor(this);
		EventManager.get_instance().register(ConsoleConnectEvent.class, this);
		EventManager.get_instance().register(CloseCommandLineWindowEvent.class, this);
		
		root = "maps/campaign/level1/filesystem.json";

		fileSystemManager = new FileSystemManager();
		fileSystemManager.addFileSystem(root);

		windows = new Array<CommandLineInterface>();
		maxWindows = 5;

		this.getRoot().addCaptureListener(new InputListener()
		{
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				boolean foundFocus = false;

				for (CommandLineInterface window : windows)
				{
					foundFocus = window.touchDown(event, x, y, pointer, button);
					if (foundFocus)
					{
						previousFocus = window;
						break;
					}
				}

				if (!foundFocus)
				{
					getOuter().setKeyboardFocus(null);
					getOuter().setScrollFocus(null);
				}

				return false;
			}
			
			public boolean keyDown(InputEvent event, int keycode)
			{
				for (CommandLineInterface window : windows)
				{
					if (window.hasKeyboardFocus())
					{
						if (keycode == Input.Keys.ESCAPE)
						{
							getOuter().setKeyboardFocus(null);
							getOuter().setScrollFocus(null);
						}
						return true;
					}
				}
				if (keycode == Input.Keys.ENTER)
				{
					if (previousFocus != null)
					{
						previousFocus.setKeyboardFocus();
						return true;
					}
				}
				return false;
			}
		});
		
		this.setKeyboardFocus(null);

		generateUI();
	}

	public UserInterface getOuter()
	{
		return this;
	}

	public void resize(int width, int height)
	{
		this.getViewport().setWorldSize(width, height);
		this.getViewport().update(width, height, true);
	}

	public void addCLI()
	{	
		if (windows.size < maxWindows)
		{
			CommandLineInterface newInterface = new CommandLineInterface(this, fileSystemManager.getFileSystem(root));
			windows.add(newInterface);
			previousFocus = newInterface;
		}
	}

	public void generateUI()
	{
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		newTerminalWindow = new Button(skin);
		newTerminalWindow.setBounds(10, 10, 64, 64);
		newTerminalWindow.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				addCLI();
			}
		});
		this.addActor(newTerminalWindow);
	}

	@Override
	public boolean keyUp(int keycode)
	{
		super.keyUp(keycode);
		for (int x = 0; x < windows.size; x++)
		{
			windows.get(x).keyUp(keycode);
		}
		return false;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		for (CommandLineInterface e : windows)
		{
			e.act();
		}
	}
	
	public void handleCloseCommandLineWindow(CloseCommandLineWindowEvent event)
	{
		Iterator<CommandLineInterface> iterator = windows.iterator();
		while(iterator.hasNext())
		{
			CommandLineInterface window = iterator.next();
			if (event.getOwnerUI().equals(window.getUUID()))
			{
				iterator.remove();
			}
		}
	}

	public void handleConsoleConnect(ConsoleConnectEvent event)
	{
		for (CommandLineInterface e : windows)
		{
			if (event.getOwnerUI().equals(e.getUUID()))
			{
				if (event.getNewRoot() == null)
				{
					e.setRoot(fileSystemManager.getFileSystem(root));
				}
				else
				{
					e.setRoot(event.getNewRoot());
				}
			}
		}

	}

}
