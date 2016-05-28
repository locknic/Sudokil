package com.custardgames.sudokil.ui;

import java.util.EventListener;
import java.util.Random;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.custardgames.sudokil.events.commandLine.AutocompleteRequestEvent;
import com.custardgames.sudokil.events.commandLine.AutocompleteResponseEvent;
import com.custardgames.sudokil.events.commandLine.CommandLineEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleLogEvent;
import com.custardgames.sudokil.managers.CommandLineManager;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.ui.cli.RootCLI;
import com.custardgames.sudokil.utils.CircularArray;

public class CommandLineInterface implements EventListener
{
	private Stage stage;
	private UUID ownerUI;

	private CommandLineManager parser;
	private Window dialog;
	private TextField consoleField;
	private Label consoleDialog;
	private Label consoleArrow;
	private ScrollPane consoleScroll;
	private int updateScroll;

	private CircularArray<String> previousCommands;
	private String tempStore;
	private int commandLocation;

	public CommandLineInterface(Stage stage, RootCLI root)
	{
		EventManager.get_instance().register(ConsoleLogEvent.class, this);
		EventManager.get_instance().register(AutocompleteResponseEvent.class, this);

		this.ownerUI = UUID.randomUUID();

		this.stage = stage;
		this.commandLocation = -1;
		this.tempStore = "";
		this.previousCommands = new CircularArray<String>(10);
		this.parser = new CommandLineManager(root, ownerUI);
		this.createWindow();

	}

	public UUID getUUID()
	{
		return ownerUI;
	}

	public void setRoot(RootCLI root)
	{
		parser.setRoot(root);
	}

	public void createWindow()
	{
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		TextButton closeButton = new TextButton("X", skin);

		Random random = new Random();

		dialog = new Window("Terminal", skin);
		dialog.setBounds(10 + random.nextInt(50), 100 + random.nextInt(50), 400, 200);
		dialog.setResizable(true);
		dialog.setKeepWithinStage(true);
		dialog.getTitleTable().add(closeButton).height(dialog.getPadTop());
		dialog.left().top();
		dialog.setResizeBorder(5);

		consoleDialog = new Label("Welcome to Terminal", skin);
		consoleDialog.setWrap(true);

		consoleArrow = new Label(parser.getInputPrefix(), skin);
		consoleField = new TextField("", skin);
		consoleField.setFocusTraversal(false);

		Table scrollTable = new Table();
		scrollTable.top();
		scrollTable.add(consoleDialog).colspan(2).expandX().fill().left().top();
		scrollTable.row();
		scrollTable.add(consoleArrow).left().top();
		scrollTable.add(consoleField).expand(true, false).fill().left().top();

		consoleScroll = new ScrollPane(scrollTable);

		dialog.add(consoleScroll).fill().expand();
		this.stage.addActor(dialog);

		closeButton.addListener(new CLICloseButtonListener(this, dialog));

		stage.setKeyboardFocus(consoleField);
		stage.setScrollFocus(consoleScroll);
	}
	
	public void dispose()
	{
		dialog.remove();
		parser.dispose();
		EventManager.get_instance().deregister(ConsoleLogEvent.class, this);
		EventManager.get_instance().deregister(AutocompleteResponseEvent.class, this);
	}
	
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		if (event.getTarget() == dialog || event.getTarget() == consoleScroll || event.getTarget() == consoleField || event.getTarget() == consoleDialog
				|| event.getTarget() == consoleArrow)
		{
			stage.setKeyboardFocus(consoleField);
			stage.setScrollFocus(consoleScroll);
			return true;
		}

		return false;
	}

	public boolean hasKeyboardFocus()
	{
		if (stage.getKeyboardFocus() == consoleField)
		{
			return true;
		}
		return false;
	}

	public void setKeyboardFocus()
	{
		stage.setKeyboardFocus(consoleField);
		stage.setScrollFocus(consoleScroll);
	}

	public void enterClicked()
	{
		EventManager.get_instance().broadcast(new CommandLineEvent(ownerUI, consoleField.getText()));
		previousCommands.add(consoleField.getText());
		consoleArrow.setText(parser.getInputPrefix());
		consoleField.setText("");
		stage.setKeyboardFocus(consoleField);
		stage.setScrollFocus(consoleScroll);
	}

	public void tabClicked()
	{
		EventManager.get_instance().broadcast(new AutocompleteRequestEvent(ownerUI, consoleField.getText()));
		stage.setKeyboardFocus(consoleField);
		stage.setScrollFocus(consoleScroll);
	}

	public boolean keyUp(int keycode)
	{
		if (stage.getKeyboardFocus() == consoleField)
		{
			if (keycode == Input.Keys.ENTER)
			{
				if (consoleField.getText().equals(""))
				{
					return true;
				}
				else
				{
					tempStore = "";
					commandLocation = -1;
					enterClicked();
					return false;
				}
			}

			if (keycode == Input.Keys.TAB)
			{
				if (consoleField.getText().equals(""))
				{
					return true;
				}
				else
				{
					tabClicked();
					return false;
				}
			}

			if (keycode == Input.Keys.UP)
			{
				if (commandLocation < previousCommands.getCurrentSize() - 1)
				{
					if (commandLocation == -1)
					{
						tempStore = consoleField.getText();
					}
					commandLocation++;
					consoleField.setText((String) previousCommands.get(commandLocation));
					consoleField.setCursorPosition(consoleField.getText().length());
				}
			}
			if (keycode == Input.Keys.DOWN)
			{
				if (commandLocation > 0)
				{
					commandLocation--;
					consoleField.setText((String) previousCommands.get(commandLocation));
					consoleField.setCursorPosition(consoleField.getText().length());
				}
				else if (commandLocation == 0)
				{
					commandLocation--;
					consoleField.setText(tempStore);
					consoleField.setCursorPosition(consoleField.getText().length());
				}
			}

			updateScroll += 2;
			return true;
		}
		else
		{
			return false;
		}
	}

	public void act()
	{
		if (updateScroll > 0)
		{
			consoleScroll.setScrollPercentX(0);
			consoleScroll.setScrollPercentY(100);
			consoleScroll.updateVisualScroll();
			updateScroll--;
		}

	}

	public void handleAutocompleteResponse(AutocompleteResponseEvent event)
	{
		if (event.getOwnerUI() == ownerUI)
		{
			consoleArrow.setText(parser.getInputPrefix());
			consoleField.setText(event.getText());
			consoleField.setCursorPosition(consoleField.getText().length());
		}
	}

	public void handleConsoleLog(ConsoleLogEvent keyEvent)
	{
		if (keyEvent.getOwnerUI() == ownerUI)
		{
			consoleDialog.setText(consoleDialog.getText() + "\n" + keyEvent.getText());
			updateScroll += 2;
		}
	}

}
