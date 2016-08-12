package com.custardgames.sudokil.states;

import com.badlogic.gdx.Screen;
import com.custardgames.sudokil.Core;
import com.custardgames.sudokil.Core.Screens;
import com.custardgames.sudokil.ui.IntroScreen;

public class Start implements Screen
{
	private Core core;
	private IntroScreen introScreen;
	
	public Start(Core core)
	{
		this.core = core;
		
		introScreen = new IntroScreen(this);
	}
	
	@Override
	public void show()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta)
	{
		introScreen.act(delta);
		introScreen.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		introScreen.resize(width, height);
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
	public void hide()
	{
		
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}
	
	public void startButtonPressed()
	{
		core.changeScreen(Screens.PLAY);
	}
	
}
