package com.custardgames.sudokil.ui;

import java.util.Calendar;
import java.util.EventListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.custardgames.sudokil.events.commandLine.DialogueEvent;
import com.custardgames.sudokil.events.ui.ToggleDialogueWindowEvent;
import com.custardgames.sudokil.managers.EventManager;

public class DialogueInterface implements EventListener
{
	private Stage stage;

	private Window dialog;
	private Label consoleDialog;
	private ScrollPane consoleScroll;
	private Button dialogueWindowButton;

	private int updateScroll;

	public DialogueInterface(Stage stage)
	{
		EventManager.get_instance().register(DialogueEvent.class, this);
		EventManager.get_instance().register(ToggleDialogueWindowEvent.class, this);
		this.stage = stage;
		createWindow();
	}

	public void dispose()
	{
		EventManager.get_instance().deregister(DialogueEvent.class, this);
	}

	public void createWindow()
	{
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		TextButton closeButton = new TextButton("", skin, "min-toggle");

		dialog = new Window("irc", skin);
		dialog.setBounds(stage.getWidth() - 410, 10, 400, 200);
		dialog.setResizable(true);
		dialog.setKeepWithinStage(true);
		dialog.getTitleTable().add(closeButton).size(dialog.getPadTop() * 4 / 5, dialog.getPadTop() * 4 / 5).padRight(dialog.getPadRight());
		dialog.left().top();
		dialog.setResizeBorder(5);
		dialog.padRight(0);
		dialog.padBottom(0);

		consoleDialog = new Label("Connecting... Connection successful", skin);
		consoleDialog.setWrap(true);
		consoleDialog.setAlignment(Align.topLeft, Align.topLeft);

		Table scrollTable = new Table();
		scrollTable.top();
		scrollTable.add(consoleDialog).expandX().fill().left().top();
		scrollTable.row();

		closeButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				hideWindow();
			}
		});

		consoleScroll = new ScrollPane(scrollTable, skin);
		consoleScroll.setFadeScrollBars(false);
		consoleScroll.setVariableSizeKnobs(true);
		dialog.add(consoleScroll).fill().expand();
		this.stage.addActor(dialog);

		dialogueWindowButton = new Button(skin);
		dialogueWindowButton.setBounds(stage.getWidth() - 74, 10, 64, 64);
		dialogueWindowButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				showWindow();
			}
		});
		stage.addActor(dialogueWindowButton);

		hideWindow();
	}
	
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		if (event.getTarget() == dialog || event.getTarget() == consoleScroll || event.getTarget() == consoleDialog)
		{
			stage.setScrollFocus(consoleScroll);
			return true;
		}

		return false;
	}

	public boolean mouseMoved(InputEvent event, float x, float y)
	{
		if (event.getTarget() == dialog || event.getTarget() == consoleScroll || event.getTarget() == consoleDialog)
		{
			stage.setScrollFocus(consoleScroll);
			return true;
		}
		return false;
	}

	public void setScrollFocus()
	{
		stage.setScrollFocus(consoleScroll);
	}

	public void resize(int width, int height)
	{
		dialogueWindowButton.setBounds(stage.getWidth() - 74, 10, 64, 64);
		dialog.setBounds(stage.getWidth() - 410, 10, 400, 200);
	}

	public void hideWindow()
	{
		dialog.setVisible(false);
		dialogueWindowButton.setVisible(true);
	}

	public void showWindow()
	{
		dialog.setVisible(true);
		dialogueWindowButton.setVisible(false);
		// dialog.setBounds(stage.getWidth() - 410, 10, 400, 200);
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

	public void handleDialogue(DialogueEvent event)
	{
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int min = Calendar.getInstance().get(Calendar.MINUTE);
		consoleDialog
				.setText(consoleDialog.getText() + "\n" + "[" + String.format("%02d", hour) + ":" + String.format("%02d", min) + "] " + event.getDialogue());
		updateScroll += 2;
	}

	public void handleToggleDialogueWindow(ToggleDialogueWindowEvent event)
	{
		if (event.isOpen())
		{
			showWindow();
		}
		else
		{
			hideWindow();
		}
	}
}
