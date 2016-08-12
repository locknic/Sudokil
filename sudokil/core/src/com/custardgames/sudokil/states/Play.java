package com.custardgames.sudokil.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Json;
import com.custardgames.sudokil.managers.TimerManager;
import com.custardgames.sudokil.ui.EndScreen;
import com.custardgames.sudokil.ui.EscapeMenu;
import com.custardgames.sudokil.ui.IntroScreen;
import com.custardgames.sudokil.ui.MapInterface;
import com.custardgames.sudokil.ui.UserInterface;

public class Play implements Screen
{
	private TimerManager timerManager;

	private LevelData levelData;
	private MapInterface mapWorld;
	private UserInterface ui;
	private EscapeMenu escapeMenu;
	private IntroScreen introScreen;
	private EndScreen endScreen;

	public static final float TICK_STEP = 1 / 60f;
	private float tickCounter;
	private float frameCounter;
	private float secondCounter;

	public Play(String levelDataLocation)
	{
		timerManager = new TimerManager();

		Json json = new Json();
		JsonTags jsonTags = json.fromJson(JsonTags.class, Gdx.files.internal("data/tags.json"));
		jsonTags.addTags(json);
		levelData = json.fromJson(LevelData.class, Gdx.files.internal(levelDataLocation));
		ui = new UserInterface(levelData);
		mapWorld = new MapInterface(levelData);
		escapeMenu = new EscapeMenu();
		introScreen = new IntroScreen();
		endScreen = new EndScreen();
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
		introScreen.resize(width, height);
		endScreen.resize(width, height);
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
			introScreen.act(dt);
			endScreen.act(dt);
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
		introScreen.draw();
		endScreen.draw();
	}

}
