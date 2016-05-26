package com.custardgames.sudokil.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.custardgames.sudokil.Core;
import com.custardgames.sudokil.managers.TimerManager;
import com.custardgames.sudokil.ui.EscapeMenu;
import com.custardgames.sudokil.ui.MapInterface;
import com.custardgames.sudokil.ui.UserInterface;

public class Play implements Screen
{
	private TimerManager timerManager;
	private MapInterface mapWorld;
	private UserInterface ui;
	private EscapeMenu escapeMenu;

	public static final float TICK_STEP = 1 / 60f;
	private float tickCounter;
	private float frameCounter;
	private float secondCounter;

	public Play()
	{
		timerManager = new TimerManager();
		ui = new UserInterface();
		mapWorld = new MapInterface();
		escapeMenu = new EscapeMenu();
		tickCounter = TICK_STEP;
	}

	@Override
	public void render(float delta)
	{
		tickCounter += delta;
		while (tickCounter >= TICK_STEP)
		{
			tickCounter -= TICK_STEP;
			update(TICK_STEP);
		}

		render();
		frameCounter++;

		if (secondCounter > 1)
		{
			System.out.println("Frames Per Second: " + frameCounter);
			secondCounter--;
			frameCounter = 0;
		}
		secondCounter += Gdx.graphics.getDeltaTime();
	}

	@Override
	public void resize(int width, int height)
	{
		mapWorld.resize(width, height);
		ui.resize(width, height);
		escapeMenu.resize(width, height);
	}

	@Override
	public void show()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void hide()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose()
	{
		ui.dispose();
		mapWorld.dispose();
		escapeMenu.dispose();
		timerManager.dispose();
	}

	public void update(float dt)
	{
		if (!escapeMenu.isInMenu())
		{
			timerManager.update(dt);
			mapWorld.update(dt);
			ui.act(dt);
		}
		else
		{
			escapeMenu.act(dt);
		}
	}

	public void render()
	{
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mapWorld.render();
		ui.draw();
		if (escapeMenu.isInMenu())
		{
			escapeMenu.draw();
		}
	}
}
