package com.custardgames.sudokil.ui;

import java.util.EventListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

		TextButton closeButton = new TextButton("X", skin);

		dialog = new Window("Custard Chat", skin);
		dialog.setBounds(stage.getWidth() - 410, 10, 400, 200);
		dialog.setResizable(true);
		dialog.setKeepWithinStage(true);
		dialog.getTitleTable().add(closeButton).height(dialog.getPadTop());
		dialog.left().top();
		dialog.setResizeBorder(5);

		consoleDialog = new Label("Connecting... \n Connection succesful", skin);
		consoleDialog.setWrap(true);

		Table scrollTable = new Table();
		scrollTable.top();
		scrollTable.add(consoleDialog).colspan(2).expandX().fill().left().top();
		scrollTable.row();

		closeButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				hideWindow();
			}
		});

		consoleScroll = new ScrollPane(scrollTable);

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
		dialog.setBounds(stage.getWidth() - 410, 10, 400, 200);
	}

	public void handleDialogue(DialogueEvent event)
	{
		consoleDialog.setText(consoleDialog.getText() + "\n" + event.getDialogue());
	}
	
	public void handleToggleDialogueWindow(ToggleDialogueWindowEvent event)
	{
		showWindow();
	}
}
