package com.custardgames.sudokil.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
	private TextButton exitButton;

	private static int WIDTH = 128;
	private static int HEIGHT = 64;

	private boolean inMenu;

	public EscapeMenu()
	{
		InputManager.get_instance().addProcessor(1, this);

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

		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		exitButton = new TextButton("EXIT", skin);
		exitButton.setBounds(this.getWidth() / 2 - (WIDTH / 2), this.getHeight() / 2 - (HEIGHT / 2), WIDTH, HEIGHT);
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

	public void resize(int width, int height)
	{
		this.getViewport().setWorldSize(width, height);
		this.getViewport().update(width, height, true);
		exitButton.setBounds(this.getWidth() / 2 - (WIDTH / 2), this.getHeight() / 2 - (HEIGHT / 2), WIDTH, HEIGHT);
	}

	public boolean isInMenu()
	{
		return inMenu;
	}

	public void show()
	{
		Gdx.input.setInputProcessor(this);
		inMenu = true;
		exitButton.setVisible(true);
	}

	public void hide()
	{
		Gdx.input.setInputProcessor(InputManager.get_instance());
		inMenu = false;
		exitButton.setVisible(false);
	}

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
