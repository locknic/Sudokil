package com.custardgames.sudokil.ui;

import java.util.EventListener;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.events.PingAssetsEvent;
import com.custardgames.sudokil.events.physicalinput.KeyPressedEvent;
import com.custardgames.sudokil.events.physicalinput.KeyReleasedEvent;
import com.custardgames.sudokil.events.physicalinput.MousePressedEvent;
import com.custardgames.sudokil.events.physicalinput.MouseReleasedEvent;
import com.custardgames.sudokil.events.physicalinput.MouseWheelMovedEvent;
import com.custardgames.sudokil.events.ui.ToggleMapRenderEvent;
import com.custardgames.sudokil.managers.ArtemisWorldManager;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.managers.InputManager;
import com.custardgames.sudokil.managers.MapManager;
import com.custardgames.sudokil.states.PlayLoadAssets;

public class MapInterface extends Stage implements EventListener
{
	private AssetManager assetManager;
	private ArtemisWorldManager worldManager;

	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	private OrthographicCamera camera;

	private Actor map;

	private Array<String> kcInput;

	private int mouseX, mouseY, mouseWheelRotation;
	private boolean mouseLeft, mouseRight, mouseMiddle;
	private boolean shouldRender;

	public MapInterface()
	{
		init();
	}

	public void init()
	{
		InputManager.get_instance().addProcessor(this);
		EventManager.get_instance().register(PingAssetsEvent.class, this);
		EventManager.get_instance().register(ToggleMapRenderEvent.class, this);

		kcInput = new Array<String>();
		mouseX = mouseY = mouseWheelRotation = 0;
		mouseLeft = mouseRight = mouseMiddle = false;

		assetManager = new PlayLoadAssets().loadAssets(new AssetManager());

		tileMap = new TmxMapLoader().load("maps/test.tmx");
		new MapManager(tileMap);
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		camera = new OrthographicCamera();
		tmr.setView(camera);
		this.getViewport().setCamera(camera);

		worldManager = new ArtemisWorldManager(camera, assetManager);

		map = new Actor();
		map.setSize(camera.viewportWidth, camera.viewportHeight);
		this.addActor(map);
	}

	public void update(float dt)
	{
		worldManager.update(dt);
		this.act(dt);
	}

	public void render()
	{
		if (shouldRender)
		{
			camera.update();
			tmr.setView(camera);
			tmr.render();
			Batch spriteBatch = getBatch();
			spriteBatch.setProjectionMatrix(camera.combined);
			worldManager.render(spriteBatch);
			this.draw();
		}
	}

	public void resize(int width, int height)
	{
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
		map.setSize(camera.viewportWidth, camera.viewportHeight);
	}

	public void releaseAll()
	{
		init();
	}

	public Array<String> getKbInput()
	{
		return kcInput;
	}

	public boolean checkKbInput(String keyCode)
	{
		for (int inputIndex = 0; inputIndex < kcInput.size; inputIndex++)
		{
			if (kcInput.get(inputIndex) != null)
			{
				if (kcInput.get(inputIndex).equals(keyCode))
				{
					return true;
				}
			}
		}
		return false;
	}

	public int getMouseX()
	{
		return mouseX;
	}

	public int getMouseY()
	{
		return mouseY;
	}

	public boolean getMouseLeft()
	{
		return mouseLeft;
	}

	public boolean getMouseRight()
	{
		return mouseRight;
	}

	public boolean getMouseMiddle()
	{
		return mouseMiddle;
	}

	public int getWheelRotation()
	{
		int oldMouseWheelRotation = mouseWheelRotation;
		mouseWheelRotation = 0;
		return oldMouseWheelRotation;
	}

	@Override
	public boolean keyDown(int keycode)
	{
		super.keyDown(keycode);

		if (!kcInput.contains("" + keycode, false))
		{
			kcInput.add(keycode + "");
		}
		EventManager.get_instance().broadcast(new KeyPressedEvent(keycode));

		return true;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		super.keyUp(keycode);

		if (kcInput.contains("" + keycode, false))
		{
			kcInput.removeValue("" + keycode, false);
		}
		EventManager.get_instance().broadcast(new KeyReleasedEvent(keycode));

		return true;
	}

	@Override
	public boolean keyTyped(char character)
	{
		super.keyTyped(character);

		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		super.touchDown(screenX, screenY, pointer, button);

		if (button == 0)
		{
			mouseLeft = true;
		}
		if (button == 1)
		{
			mouseRight = true;
		}
		if (button == 2)
		{
			mouseMiddle = true;
		}

		EventManager.get_instance().broadcast(new MousePressedEvent(button));
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		super.touchUp(screenX, screenY, pointer, button);

		if (button == 0)
		{
			mouseLeft = false;
		}
		if (button == 1)
		{
			mouseRight = false;
		}
		if (button == 2)
		{
			mouseMiddle = false;
		}

		EventManager.get_instance().broadcast(new MouseReleasedEvent(button));
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		super.touchDragged(screenX, screenY, pointer);

		mouseX = screenX;
		mouseY = screenY;
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		super.mouseMoved(screenX, screenY);

		mouseX = screenX;
		mouseY = screenY;
		return true;
	}

	@Override
	public boolean scrolled(int amount)
	{
		super.scrolled(amount);

		mouseWheelRotation = amount;
		EventManager.get_instance().broadcast(new MouseWheelMovedEvent(amount));
		return true;
	}

	public PingAssetsEvent handleInquiryPingAssetsEvent(PingAssetsEvent event)
	{
		Object asset = assetManager.get(event.getAssetLocation());
		event.setAsset(asset);
		return event;
	}

	public void handleToggleMapRender(ToggleMapRenderEvent event)
	{
		this.shouldRender = event.isShouldRender();
	}

}
