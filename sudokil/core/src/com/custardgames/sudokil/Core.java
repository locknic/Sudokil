package com.custardgames.sudokil;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.managers.InputManager;
import com.custardgames.sudokil.states.Play;

public class Core extends Game
{
	public static final String TITLE = "Sudokil";
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int SCALE = 1;

	private Array<Screen> screens;

	@Override
	public void create()
	{
		Gdx.input.setInputProcessor(InputManager.get_instance());

		screens = new Array<Screen>();
		screens.add(new Play());
		setScreen(screens.get(0));
	}

}
