package com.custardgames.sudokil.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.custardgames.sudokil.ui.MapInterface;
import com.custardgames.sudokil.ui.UserInterface;

public class Play implements Screen
{
	private MapInterface worldInput;
	private UserInterface ui;

	public static final float TICK_STEP = 1 / 60f;
	private float tickCounter;
	private float frameCounter;
	private float secondCounter;

	public Play()
	{
		ui = new UserInterface();
		worldInput = new MapInterface();
	}

	@Override
	public void render(float delta)
	{
		tickCounter += Gdx.graphics.getDeltaTime();
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
		worldInput.resize(width, height);
		ui.resize(width, height);
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
		worldInput.dispose();
	}

	public void update(float dt)
	{
		worldInput.update(dt);
		ui.act(dt);
	}

	public void render()
	{
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		worldInput.render();
		ui.draw();
	}
}
