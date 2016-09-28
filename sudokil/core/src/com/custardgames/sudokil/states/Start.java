package com.custardgames.sudokil.states;

import com.badlogic.gdx.Screen;
import com.custardgames.sudokil.Core;
import com.custardgames.sudokil.ui.screens.IntroScreen;

public class Start implements Screen
{
	private IntroScreen introScreen;
	
	public Start(Core core)
	{
		introScreen = new IntroScreen(core);
	}
	
	@Override
	public void show()
	{
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
		introScreen.hide();
	}

	@Override
	public void dispose()
	{
		introScreen.dispose();
	}
}
