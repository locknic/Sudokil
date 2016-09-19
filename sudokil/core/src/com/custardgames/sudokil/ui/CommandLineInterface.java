package com.custardgames.sudokil.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.Random;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.events.commandLine.AutocompleteRequestEvent;
import com.custardgames.sudokil.events.commandLine.AutocompleteResponseEvent;
import com.custardgames.sudokil.events.commandLine.ClearTerminalEvent;
import com.custardgames.sudokil.events.commandLine.CommandLineEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleLogEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.ui.cli.RootCLI;

public class CommandLineInterface implements EventListener
{
	private Stage stage;

	private Window dialog;
	private TextField consoleField;
	private Label consoleDialog;
	private Label consoleArrow;
	private ScrollPane consoleScroll;
	private int updateScroll;

	private CommandLineData cld;

	public CommandLineInterface(Stage stage, RootCLI root)
	{
		EventManager.get_instance().register(ConsoleLogEvent.class, this);
		EventManager.get_instance().register(AutocompleteResponseEvent.class, this);
		EventManager.get_instance().register(ClearTerminalEvent.class, this);

		this.cld = new CommandLineData(root, null);
		this.stage = stage;
		this.createWindow();
	}

	public UUID getUUID()
	{
		return cld.ownerUI;
	}
	
	public Array<UUID> getAllUUIDs()
	{
		return cld.getAllUUIDs();
	}

	public void tryAllUpRoot(UUID cliID)
	{
		CommandLineData newData = cld.findCommandLineData(cliID);
		if (newData != null)
		{
			cld = newData;
			consoleArrow.setText(cld.parser.getInputPrefix());
			consoleDialog.setText(cld.textHistory);
			updateScroll += 5;
		}
	}

	public void setRoot(RootCLI root)
	{
		CommandLineData newData = new CommandLineData(root, cld);
		cld = newData;

		SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
		String day = simpleDateformat.format(new Date());
		String month = new SimpleDateFormat("MMM").format(Calendar.getInstance().getTime());
		int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int min = Calendar.getInstance().get(Calendar.MINUTE);
		int sec = Calendar.getInstance().get(Calendar.SECOND);
		String textTest = "Connected to " + root.getDeviceName() + ": " + day + " " + month + " " + String.format("%02d", date) + " "
				+ String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);

		cld.textHistory = textTest;
		consoleArrow.setText(cld.parser.getInputPrefix());
		consoleDialog.setText(cld.textHistory);
	}

	public void createWindow()
	{
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		TextButton closeButton = new TextButton("", skin, "close-toggle");

		Random random = new Random();

		dialog = new Window("Terminal", skin);
		dialog.setBounds(10 + random.nextInt(50), 100 + random.nextInt(50), 400, 200);
		dialog.setResizable(true);
		dialog.setKeepWithinStage(true);
		dialog.getTitleTable().add(closeButton).size(dialog.getPadTop() * 4 / 5, dialog.getPadTop() * 4 / 5).padRight(dialog.getPadRight());
		dialog.left().top();
		dialog.setResizeBorder(5);
		dialog.padRight(0);
		dialog.padBottom(1);

		SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
		String day = simpleDateformat.format(new Date());
		String month = new SimpleDateFormat("MMM").format(Calendar.getInstance().getTime());
		int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int min = Calendar.getInstance().get(Calendar.MINUTE);
		int sec = Calendar.getInstance().get(Calendar.SECOND);
		String textTest = "Last login: " + day + " " + month + " " + String.format("%02d", date) + " " + String.format("%02d", hour) + ":"
				+ String.format("%02d", min) + ":" + String.format("%02d", sec);

		cld.textHistory = textTest;
		consoleDialog = new Label(cld.textHistory, skin);
		consoleDialog.setWrap(true);
		consoleDialog.setAlignment(Align.topLeft, Align.topLeft);

		consoleArrow = new Label(cld.parser.getInputPrefix(), new LabelStyle(skin.get(LabelStyle.class)));
		consoleField = new TextField("", skin);
		consoleField.setFocusTraversal(false);
		Color colour = Color.ORANGE;
		colour.a = 0.8f;
		consoleField.getStyle().cursor = skin.newDrawable("white", colour);
		consoleField.getStyle().cursor.setMinWidth(10);
		consoleField.setBlinkTime(0.6f);

		Table scrollTable = new Table();
		scrollTable.top();
		scrollTable.add(consoleDialog).colspan(2).growX().fill().left().top();
		scrollTable.row();
		scrollTable.add(consoleArrow).left().top();
		scrollTable.add(consoleField).expand(true, false).fill().left().top();
		scrollTable.padBottom(1);

		consoleScroll = new ScrollPane(scrollTable, skin);
		consoleScroll.setFadeScrollBars(false);
		consoleScroll.setVariableSizeKnobs(true);
		consoleScroll.setFlickScroll(false);
		dialog.add(consoleScroll).fill().expand();
		this.stage.addActor(dialog);

		closeButton.addListener(new CLICloseButtonListener(this, dialog));

		setKeyboardFocus();
	}

	public void dispose()
	{
		dialog.remove();
		cld.parser.dispose();
		EventManager.get_instance().deregister(ConsoleLogEvent.class, this);
		EventManager.get_instance().deregister(AutocompleteResponseEvent.class, this);
		EventManager.get_instance().deregister(ClearTerminalEvent.class, this);
	}

	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		if (event.getTarget() == dialog || event.getTarget() == consoleScroll || event.getTarget() == consoleField || event.getTarget() == consoleDialog
				|| event.getTarget() == consoleArrow)
		{
			setKeyboardFocus();
			return true;
		}

		return false;
	}

	public boolean mouseMoved(InputEvent event, float x, float y)
	{
		if (event.getTarget() == dialog || event.getTarget() == consoleScroll || event.getTarget() == consoleField || event.getTarget() == consoleDialog
				|| event.getTarget() == consoleArrow)
		{
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

		cld.parser.highlightEntities(consoleField.getText());
	}

	public void setScrollFocus()
	{
		stage.setScrollFocus(consoleScroll);
	}

	public void enterClicked()
	{
		EventManager.get_instance().broadcast(new CommandLineEvent(cld.ownerUI, consoleField.getText()));
		cld.previousCommands.add(consoleField.getText());
		consoleArrow.setText(cld.parser.getInputPrefix());
		consoleField.setText("");
		setKeyboardFocus();
	}

	public void tabClicked()
	{
		EventManager.get_instance().broadcast(new AutocompleteRequestEvent(cld.ownerUI, consoleField.getText()));
		setKeyboardFocus();
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
					cld.tempStore = "";
					cld.commandLocation = -1;
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
				if (cld.commandLocation < cld.previousCommands.getCurrentSize() - 1)
				{
					if (cld.commandLocation == -1)
					{
						cld.tempStore = consoleField.getText();
					}
					cld.commandLocation++;
					consoleField.setText(cld.previousCommands.get(cld.commandLocation));
					consoleField.setCursorPosition(consoleField.getText().length());
				}
			}
			if (keycode == Input.Keys.DOWN)
			{
				if (cld.commandLocation > 0)
				{
					cld.commandLocation--;
					consoleField.setText(cld.previousCommands.get(cld.commandLocation));
					consoleField.setCursorPosition(consoleField.getText().length());
				}
				else if (cld.commandLocation == 0)
				{
					cld.commandLocation--;
					consoleField.setText(cld.tempStore);
					consoleField.setCursorPosition(consoleField.getText().length());
				}
			}

			updateScroll += 5;
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
		if (event.getOwnerUI() == cld.ownerUI)
		{
			consoleArrow.setText(cld.parser.getInputPrefix());
			consoleField.setText(event.getText());
			consoleField.setCursorPosition(consoleField.getText().length());
		}
	}

	public void handleConsoleLog(ConsoleLogEvent event)
	{
		if (event.getOwnerUI() == cld.ownerUI)
		{
			cld.textHistory = cld.textHistory + "\n" + event.getText();
			consoleDialog.setText(cld.textHistory);
			updateScroll += 20;
		}
	}

	public void handleClearTerminal(ClearTerminalEvent event)
	{
		if (event.getOwnerUI() == cld.ownerUI)
		{
			cld.textHistory = "";
			consoleDialog.setText(cld.textHistory);
			updateScroll += 20;
		}
	}

}
