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
import com.badlogic.gdx.utils.Json;
import com.custardgames.sudokil.events.ChangeLevelEvent;
import com.custardgames.sudokil.events.commandLine.CloseCommandLineWindowEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleConnectEvent;
import com.custardgames.sudokil.events.ui.TerminalOpenedEvent;
import com.custardgames.sudokil.events.ui.ToggleTerminalButtonEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.managers.FileSystemManager;
import com.custardgames.sudokil.managers.InputManager;
import com.custardgames.sudokil.states.JsonTags;
import com.custardgames.sudokil.states.LevelData;

public class UserInterface extends Stage implements EventListener
{
	private String root;
	private Array<CommandLineInterface> windows;
	private CommandLineInterface previousFocus;
	private int maxWindows;
	private Button newTerminalWindow;
	private DialogueInterface dialogueInterface;
	private FileSystemManager fileSystemManager;

	public UserInterface(LevelData levelData)
	{
		InputManager.get_instance().addProcessor(this);
		EventManager.get_instance().register(ConsoleConnectEvent.class, this);
		EventManager.get_instance().register(CloseCommandLineWindowEvent.class, this);
		EventManager.get_instance().register(ToggleTerminalButtonEvent.class, this);
		EventManager.get_instance().register(ChangeLevelEvent.class, this);

		root = levelData.getPlayerFilesystem();

		fileSystemManager = new FileSystemManager();
		fileSystemManager.addFileSystem(root);

		for(String newFileSystem : levelData.getFilesystems())
		{
			fileSystemManager.addFileSystem(newFileSystem);
		}
		
		windows = new Array<CommandLineInterface>();
		maxWindows = 5;

		dialogueInterface = new DialogueInterface(this);

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
	
	public void changeLevel(LevelData levelData)
	{
		fileSystemManager.deleteFileSystems();
		root = levelData.getPlayerFilesystem();
		fileSystemManager.addFileSystem(root);
		for(String newFileSystem : levelData.getFilesystems())
		{
			fileSystemManager.addFileSystem(newFileSystem);
		}
		for(CommandLineInterface cli : windows)
		{
			cli.setRoot(fileSystemManager.getFileSystem(root));
		}
	}
	
	public void dispose()
	{
		EventManager.get_instance().deregister(ConsoleConnectEvent.class, this);
		EventManager.get_instance().deregister(CloseCommandLineWindowEvent.class, this);
		EventManager.get_instance().deregister(ToggleTerminalButtonEvent.class, this);
		
		dialogueInterface.dispose();
	}

	public UserInterface getOuter()
	{
		return this;
	}

	public void resize(int width, int height)
	{
		this.getViewport().setWorldSize(width, height);
		this.getViewport().update(width, height, true);
		dialogueInterface.resize(width, height);
	}

	public void addCLI()
	{
		if (windows.size < maxWindows)
		{
			EventManager.get_instance().broadcast(new TerminalOpenedEvent());
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
		dialogueInterface.act();
	}

	public void handleCloseCommandLineWindow(CloseCommandLineWindowEvent event)
	{
		Iterator<CommandLineInterface> iterator = windows.iterator();
		while (iterator.hasNext())
		{
			CommandLineInterface window = iterator.next();
			if (event.getOwnerUI().equals(window.getUUID()))
			{
				window.dispose();
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
	
	public void handleToggleTerminalButton(ToggleTerminalButtonEvent event)
	{
		newTerminalWindow.setVisible(event.isButtonVisible());
		newTerminalWindow.setDisabled(!event.isButtonVisible());
		
		if (!event.isButtonVisible())
		{
			Iterator<CommandLineInterface> iterator = windows.iterator();
			while (iterator.hasNext())
			{
				iterator.next().dispose();
				iterator.remove();
			}
		}
	}
	
	public void handleChangeLevel(ChangeLevelEvent event)
	{		
		Json json = new Json();
		JsonTags jsonTags = json.fromJson(JsonTags.class, Gdx.files.internal("data/tags.json"));
		jsonTags.addTags(json);
		LevelData levelData = json.fromJson(LevelData.class, Gdx.files.internal(event.getLevelDataLocation()));
		changeLevel(levelData);
	}

}
