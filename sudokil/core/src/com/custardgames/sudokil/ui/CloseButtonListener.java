package com.custardgames.sudokil.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CloseButtonListener extends ClickListener
{
	private Actor actor;

	public CloseButtonListener(Actor actor)
	{
		this.actor = actor;
	}

	@Override
	public void clicked(InputEvent event, float x, float y)
	{
		actor.remove();
	}
}
