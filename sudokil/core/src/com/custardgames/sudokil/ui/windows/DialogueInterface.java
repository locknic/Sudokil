package com.custardgames.sudokil.ui.windows;

import java.util.Calendar;
import java.util.EventListener;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.events.commandLine.DialogueEvent;
import com.custardgames.sudokil.events.ui.ToggleDialogueWindowEvent;
import com.custardgames.sudokil.managers.EventManager;

public class DialogueInterface implements EventListener
{
	private Stage stage;

	private Window dialog;
	private Label consoleDialog;
	private ScrollPane consoleScroll;
	private ImageButton dialogueWindowButton;
	private Table scrollTable;
	private Skin skin;

	private Array<Label> fadingBackgrounds;

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
		fadingBackgrounds = new Array<Label>();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		TextButton closeButton = new TextButton("", skin, "min-toggle");

		dialog = new Window("CG Telnet Client", skin);
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

		scrollTable = new Table();
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
		consoleScroll.setFlickScroll(false);
		dialog.add(consoleScroll).fill().expand();
		this.stage.addActor(dialog);

		TextureAtlas buttonAtlas = new TextureAtlas("data/uibuttons.atlas");
		Skin buttonSkin = new Skin(buttonAtlas);
		ImageButtonStyle style = new ImageButtonStyle();
		style.up = buttonSkin.getDrawable("mail");
		style.down = buttonSkin.getDrawable("mail-down");

		dialogueWindowButton = new ImageButton(style);
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
		if (event.getTarget() == dialog || event.getTarget() == consoleScroll || event.getTarget() == consoleDialog || event.getTarget() instanceof Label)
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

		Iterator<Label> it = fadingBackgrounds.iterator();
		while (it.hasNext())
		{
			Label label = it.next();
			Color color = ((SpriteDrawable) label.getStyle().background).getSprite().getColor();
			float min = 0;
			if (!it.hasNext())
			{
				min = 0.1f;
			}
			if (color.a - 0.005 > min)
			{
				color.a -= 0.005;
				label.getStyle().background = ((SpriteDrawable) label.getStyle().background).tint(color);
			}
			else
			{
				color.a = min;
				label.getStyle().background = ((SpriteDrawable) label.getStyle().background).tint(color);
				if (min == 0)
				{
					it.remove();
				}
			}
		}
	}

	public void handleDialogue(DialogueEvent event)
	{
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int min = Calendar.getInstance().get(Calendar.MINUTE);
		Label newConsoleDialog = new Label("[" + String.format("%02d", hour) + ":" + String.format("%02d", min) + "] " + event.getDialogue(),
				new LabelStyle(skin.get("orange", LabelStyle.class)));
		newConsoleDialog.setWrap(true);
		newConsoleDialog.setAlignment(Align.topLeft, Align.topLeft);
		scrollTable.row();
		scrollTable.add(newConsoleDialog).expandX().fill().left().top();
		fadingBackgrounds.add(newConsoleDialog);
		updateScroll += 10;
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
