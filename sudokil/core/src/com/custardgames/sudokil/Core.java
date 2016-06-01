package com.custardgames.sudokil;

import java.util.EventListener;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.custardgames.sudokil.managers.InputManager;
import com.custardgames.sudokil.states.Play;

public class Core extends Game implements EventListener
{
	public static final String TITLE = "Sudokil";
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int SCALE = 1;
	public static final boolean FULLSCREEN = false;
	public static final boolean VSYNC = true;

	private Play playScreen;

	public Core()
	{
	}
	
	@Override
	public void create()
	{
		playScreen = new Play("maps/campaign/level1/level-data.json");
		setScreen(playScreen);
		Gdx.input.setInputProcessor(InputManager.get_instance());
	}

}
