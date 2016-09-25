package com.custardgames.sudokil.ui.screens;

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
import com.custardgames.sudokil.managers.InputManager;
import com.custardgames.sudokil.ui.windows.LevelLoaderInterface;

public class IntroScreen extends Stage
{
	private TextButton playButton;
	private TextButton exitButton;

	private static int WIDTH = 256;
	private static int HEIGHT = 64;
	private TextureAtlas buttonAtlas;
	private Image image;
	private LevelLoaderInterface levelLoaderInterface;
	
	public IntroScreen()
	{
		InputManager.get_instance().addProcessor(this);
		
		TextureAtlas skinAtlas = new TextureAtlas("data/uiskin.atlas");
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"), skinAtlas);
		playButton = new TextButton("PLAY", skin, "orange");
		playButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				levelLoaderInterface.showWindow();
			}
		});
		
		exitButton = new TextButton("EXIT", skin, "orange");
		exitButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				Gdx.app.exit();
			}
		});

		buttonAtlas = new TextureAtlas("data/uiscreens.atlas");
		image = new Image(buttonAtlas.findRegion("intro"));
		this.addActor(image);
		image.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.addActor(playButton);
		this.addActor(exitButton);
		
		levelLoaderInterface = new LevelLoaderInterface(this);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		buttonAtlas.dispose();
		InputManager.get_instance().removeProcessor(this);
	}

	public void resize(int width, int height)
	{
		this.getViewport().setWorldSize(width, height);
		this.getViewport().update(width, height, true);
		playButton.setBounds(this.getWidth() / 2 - (WIDTH / 2), this.getHeight() / 4 - (HEIGHT / 2), WIDTH, HEIGHT);
		exitButton.setBounds(this.getWidth() / 2 - (WIDTH / 2), this.getHeight() / 4 - (HEIGHT / 2) - HEIGHT - 10, WIDTH, HEIGHT);
		image.setBounds(0, 0, width, height);
	}

	public void hide()
	{
		playButton.remove();
		exitButton.remove();
		image.remove();
		InputManager.get_instance().removeProcessor(this);
	}
	
	@Override
	public void draw()
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
