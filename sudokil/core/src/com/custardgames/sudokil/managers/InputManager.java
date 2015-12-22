package com.custardgames.sudokil.managers;

import com.badlogic.gdx.InputMultiplexer;

public class InputManager extends InputMultiplexer
{
	private static InputMultiplexer _instance = new InputMultiplexer();

	public static InputMultiplexer get_instance()
	{
		return _instance;
	}
}
