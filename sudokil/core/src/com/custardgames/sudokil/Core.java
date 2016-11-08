package com.custardgames.sudokil;

import java.util.EventListener;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.custardgames.sudokil.managers.InputManager;
import com.custardgames.sudokil.managers.UniTweenManager;
import com.custardgames.sudokil.states.Play;
import com.custardgames.sudokil.states.Start;

public class Core extends Game implements EventListener
{
	public static final String TITLE = "Sudokil alpha v0.23";
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int SCALE = 1;
	public static final boolean FULLSCREEN = false;
	public static final boolean VSYNC = true;
	
	public enum Screens
	{
		START, PLAY, END
	}

	public Core()
	{
		new UniTweenManager();
	}

	@Override
	public void dispose()
	{
		super.dispose();
		if (this.getScreen() != null)
		{
			this.getScreen().dispose();
		}
	}

	@Override
	public void create()
	{
		Gdx.input.setInputProcessor(InputManager.get_instance());
		changeScreen(Screens.START);
	}

	public void changeScreen(Screens newScreen)
	{
		if (this.getScreen() != null)
		{
			this.getScreen().dispose();
		}
		switch (newScreen)
		{
			case START:
				setScreen(new Start(this));
				break;
			case PLAY:
				setScreen(new Play("maps/campaign/level1/level-data.json"));
				break;
			case END:
				break;
		}
	}
	
}
