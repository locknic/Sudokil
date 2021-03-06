package com.custardgames.sudokil.ui;

import java.util.EventListener;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
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
import com.custardgames.sudokil.ui.windows.CommandLineInterface;
import com.custardgames.sudokil.ui.windows.DialogueInterface;
import com.custardgames.sudokil.utils.JsonTags;
import com.custardgames.sudokil.utils.LevelData;

public class UserInterface extends Stage implements EventListener
{
	private String root;
	private Array<CommandLineInterface> windows;
	private CommandLineInterface previousFocus;
	private int maxWindows;
	private ImageButton newTerminalWindow;
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

		for (String newFileSystem : levelData.getFilesystems())
		{
			fileSystemManager.addFileSystem(newFileSystem);
		}

		windows = new Array<CommandLineInterface>();
		maxWindows = 5;

		dialogueInterface = new DialogueInterface(this);

		this.getRoot().addCaptureListener(new InputListener()
		{
			@Override
			public boolean mouseMoved(InputEvent event, float x, float y)
			{
				boolean foundFocus = false;

				for (CommandLineInterface window : windows)
				{
					foundFocus = window.mouseMoved(event, x, y);
					if (foundFocus)
					{
						window.setScrollFocus();
						break;
					}
				}

				if (!foundFocus)
				{
					foundFocus = dialogueInterface.mouseMoved(event, x, y);
					if (foundFocus)
					{
						dialogueInterface.setScrollFocus();
					}
				}

				if (!foundFocus)
				{
					setScrollFocus(null);
				}

				return false;
			}

			@Override
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

			@Override
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

	public void reset()
	{
		newTerminalWindow.setVisible(true);
		newTerminalWindow.setDisabled(false);

		Iterator<CommandLineInterface> iterator = windows.iterator();
		while (iterator.hasNext())
		{
			iterator.next().dispose();
			iterator.remove();
		}
	}

	public void changeLevel(LevelData levelData)
	{
		fileSystemManager.deleteFileSystems();
		root = levelData.getPlayerFilesystem();
		fileSystemManager.addFileSystem(root);
		for (String newFileSystem : levelData.getFilesystems())
		{
			fileSystemManager.addFileSystem(newFileSystem);
		}
		for (CommandLineInterface cli : windows)
		{
			cli.setRoot(fileSystemManager.getFileSystem(root));
		}
		reset();
	}

	@Override
	public void dispose()
	{
		InputManager.get_instance().removeProcessor(this);
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
		TextureAtlas buttonAtlas = new TextureAtlas("data/uibuttons.atlas");
		Skin buttonSkin = new Skin(buttonAtlas);
		ImageButtonStyle style = new ImageButtonStyle(); // ** Button properties
															// **//
		style.up = buttonSkin.getDrawable("terminal");
		style.down = buttonSkin.getDrawable("terminal-down");
		newTerminalWindow = new ImageButton(style);
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
			if (event.getOwnerUI().getOwner().equals(window.getUUID()))
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
			if (event.getNewRoot() == null)
			{
				e.tryAllUpRoot(event.getOwnerUI().getOwner());
			}
			else if (event.getOwnerUI().getOwner().equals(e.getUUID()))
			{
				e.setRoot(event.getNewRoot());
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
		reset();
	}

}
