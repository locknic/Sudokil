package com.custardgames.sudokil.ui;

import java.util.EventListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.custardgames.sudokil.events.EndGameEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.managers.InputManager;

public class EndScreen extends Stage implements EventListener
{
	private TextButton playButton;

	private static int WIDTH = 128;
	private static int HEIGHT = 64;
	private Image image;
	private boolean alive;

	public EndScreen()
	{
		alive = false;
		EventManager.get_instance().register(EndGameEvent.class, this);
	}

	public void resize(int width, int height)
	{
		this.getViewport().setWorldSize(width, height);
		this.getViewport().update(width, height, true);
		playButton.setBounds(this.getWidth() / 2 - (WIDTH / 2), this.getHeight() / 2 - (HEIGHT / 2), WIDTH, HEIGHT);
		image.setBounds(0, 0, width, height);
	}

	public void show()
	{
		InputManager.get_instance().addProcessor(1, this);

		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		playButton = new TextButton("EXIT", skin);
		playButton.setBounds(this.getWidth() / 2 - (WIDTH / 2), this.getHeight() / 4 - (HEIGHT / 2), WIDTH, HEIGHT);
		playButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				Gdx.app.exit();
			}
		});
		
		TextureAtlas buttonAtlas = new TextureAtlas("data/uiscreens.atlas");
		image = new Image(buttonAtlas.findRegion("end"));
		this.addActor(image);
		image.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.addActor(playButton);
		alive = true;
	}

	public void draw()
	{
		if (alive)
		{
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			ShapeRenderer shapeRenderer = new ShapeRenderer();
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	
			shapeRenderer.setColor(new Color(0, 0, 0, 1f));
			shapeRenderer.rect(0, 0, this.getWidth(), this.getHeight());
	
			shapeRenderer.end();
			super.draw();
		}
	}
	
	public void handleEndGame(EndGameEvent event)
	{
		show();
	}
}
