package com.custardgames.sudokil.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.custardgames.sudokil.managers.InputManager;

import javafx.scene.input.KeyCode;

public class EscapeMenu extends Stage
{
	private TextButton exitButton;
	
	private static int WIDTH = 128;
	private static int HEIGHT = 64;
	
	private boolean inMenu;
	
	public EscapeMenu()
	{
		InputManager.get_instance().addProcessor(1, this);
		
		hide();
		
		this.getRoot().addCaptureListener(new InputListener()
		{
			public boolean keyDown(InputEvent event, int keycode)
			{
				if (keycode == Input.Keys.ESCAPE)
				{
					if (inMenu)
					{
						hide();
					}
					else
					{
						show();
					}
				}
				return false;
			}
		});
		
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		exitButton = new TextButton("EXIT", skin);
		exitButton.setBounds(this.getWidth() / 2 - (WIDTH / 2), this.getHeight() / 2 - (HEIGHT / 2), WIDTH, HEIGHT);
		exitButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				if (inMenu)
				{
					Gdx.app.exit();
				}
			}
		});
		this.addActor(exitButton);
	}
	
	public void resize(int width, int height)
	{
		this.getViewport().setWorldSize(width, height);
		this.getViewport().update(width, height, true);
		exitButton.setBounds(this.getWidth() / 2 - (WIDTH / 2), this.getHeight() / 2 - (HEIGHT / 2), WIDTH, HEIGHT);
	}
	
	public boolean isInMenu()
	{
		return inMenu;
	}
	
	public void show()
	{
		Gdx.input.setInputProcessor(this);
		inMenu = true;
	}

	public void hide()
	{
		Gdx.input.setInputProcessor(InputManager.get_instance());
		inMenu = false;
	}
}
