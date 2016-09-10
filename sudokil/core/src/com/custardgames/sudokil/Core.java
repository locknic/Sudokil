package com.custardgames.sudokil;

import java.util.EventListener;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.custardgames.sudokil.managers.InputManager;
import com.custardgames.sudokil.states.Play;
import com.custardgames.sudokil.states.Start;

public class Core extends Game implements EventListener
{
	public static final String TITLE = "Sudokil";
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int SCALE = 1;
	public static final boolean FULLSCREEN = false;
	public static final boolean VSYNC = true;

	public enum Screens
	{
		START, PLAY, END
	}

	@Override
	public void create()
	{
		Gdx.input.setInputProcessor(InputManager.get_instance());
		changeScreen(Screens.START);
	}

	public void changeScreen(Screens newScreen)
	{
		switch (newScreen)
		{
			case START:
				setScreen(new Start(this));
				break;
			case PLAY:
				setScreen(new Play("maps/playground/wireless/level-data.json"));
				break;
			case END:
				break;
		}
	}

}
