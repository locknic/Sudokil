package com.custardgames.sudokil.ui.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.custardgames.sudokil.managers.InputManager;

public class EscapeMenu extends Stage
{
	private TextButton resumeButton;
	private TextButton loadButton;
	private TextButton exitButton;

	private static int WIDTH = 256;
	private static int HEIGHT = 64;

	private boolean inMenu;
	
	private LevelLoaderInterface levelLoaderInterface;

	public EscapeMenu()
	{
		InputManager.get_instance().addProcessor(1, this);
		
		levelLoaderInterface = new LevelLoaderInterface(this);
		
		this.getRoot().addCaptureListener(new InputListener()
		{
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				if (inMenu)
				{
					return true;
				}
				return false;
			}

			@Override
			public boolean keyDown(InputEvent event, int keycode)
			{
				if (keycode == Input.Keys.ESCAPE)
				{
					if (inMenu)
					{
						hide();
					}
					else
					{
						show();
					}
				}
				return false;
			}
		});
		
		TextureAtlas skinAtlas = new TextureAtlas("data/uiskin.atlas");
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"), skinAtlas);
		resumeButton = new TextButton("RESUME", skin, "orange");
		resumeButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				hide();
			}
		});
		
		loadButton = new TextButton("LOAD LEVEL", skin, "orange");
		loadButton.addListener(new ChangeListener()
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
				if (inMenu)
				{
					Gdx.app.exit();
				}
			}
		});
		this.addActor(exitButton);

		hide();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		InputManager.get_instance().removeProcessor(this);
	}

	public void resize(int width, int height)
	{
		this.getViewport().setWorldSize(width, height);
		this.getViewport().update(width, height, true);
		resumeButton.setBounds(this.getWidth() / 2 - (WIDTH / 2), this.getHeight() / 2 - (HEIGHT / 2) + HEIGHT + 10, WIDTH, HEIGHT);
		loadButton.setBounds(this.getWidth() / 2 - (WIDTH / 2), this.getHeight() / 2 - (HEIGHT / 2), WIDTH, HEIGHT);
		exitButton.setBounds(this.getWidth() / 2 - (WIDTH / 2), this.getHeight() / 2 - (HEIGHT / 2) - HEIGHT - 10, WIDTH, HEIGHT);
	}

	public boolean isInMenu()
	{
		return inMenu;
	}
	
	public void show()
	{
		Gdx.input.setInputProcessor(this);
		inMenu = true;
		resumeButton.setVisible(true);
		this.addActor(resumeButton);
		loadButton.setVisible(true);
		this.addActor(loadButton);
		exitButton.setVisible(true);
		this.addActor(exitButton);
	}

	public void hide()
	{
		Gdx.input.setInputProcessor(InputManager.get_instance());
		inMenu = false;
		resumeButton.setVisible(false);
		resumeButton.remove();
		loadButton.setVisible(false);
		loadButton.remove();
		exitButton.setVisible(false);
		exitButton.remove();
		levelLoaderInterface.hideWindow();
	}

	@Override
	public void draw()
	{
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		shapeRenderer.setColor(new Color(0, 0, 0, 0.75f));
		shapeRenderer.rect(0, 0, this.getWidth(), this.getHeight());

		shapeRenderer.end();
		super.draw();
	}

}
