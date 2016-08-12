package com.custardgames.sudokil.ui;

import java.util.EventListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.custardgames.sudokil.events.ChangeLevelEvent;
import com.custardgames.sudokil.events.ChangeMapEvent;
import com.custardgames.sudokil.events.ChangedMapEvent;
import com.custardgames.sudokil.events.PingAssetsEvent;
import com.custardgames.sudokil.events.physicalinput.KeyPressedEvent;
import com.custardgames.sudokil.events.physicalinput.KeyReleasedEvent;
import com.custardgames.sudokil.events.physicalinput.MouseDraggedEvent;
import com.custardgames.sudokil.events.physicalinput.MousePressedEvent;
import com.custardgames.sudokil.events.physicalinput.MouseReleasedEvent;
import com.custardgames.sudokil.events.physicalinput.MouseWheelMovedEvent;
import com.custardgames.sudokil.events.ui.ToggleMapRenderEvent;
import com.custardgames.sudokil.managers.ArtemisWorldManager;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.managers.InputManager;
import com.custardgames.sudokil.managers.MapManager;
import com.custardgames.sudokil.states.JsonTags;
import com.custardgames.sudokil.states.LevelData;

public class MapInterface extends Stage implements EventListener
{
	private AssetManager assetManager;
	private ArtemisWorldManager worldManager;

	private TiledMap tileMap;
	private MapManager mapManager;
	private OrthogonalTiledMapRenderer tmr;
	private OrthographicCamera camera;

	private Actor map;

	private Array<String> kcInput;

	private int mouseX, mouseY, mouseWheelRotation;
	private boolean mouseLeft, mouseRight, mouseMiddle;
	private boolean shouldRender;

	public MapInterface(LevelData levelData)
	{
		InputManager.get_instance().addProcessor(this);
		EventManager.get_instance().register(PingAssetsEvent.class, this);
		EventManager.get_instance().register(ToggleMapRenderEvent.class, this);
		EventManager.get_instance().register(ChangeLevelEvent.class, this);
		EventManager.get_instance().register(ChangeMapEvent.class, this);

		kcInput = new Array<String>();
		mouseX = mouseY = mouseWheelRotation = 0;
		mouseLeft = mouseRight = mouseMiddle = false;

		assetManager = new AssetManager();
		loadAssets(assetManager, levelData);

		tileMap = new TmxMapLoader().load(levelData.getMapLocation());
		mapManager = new MapManager(tileMap);
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		camera = new OrthographicCamera();
		tmr.setView(camera);
		this.getViewport().setCamera(camera);

		worldManager = new ArtemisWorldManager(camera, assetManager, levelData);

		map = new Actor();
		map.setSize(camera.viewportWidth, camera.viewportHeight);
		this.addActor(map);
	}

	@Override
	public void dispose()
	{
		super.dispose();
		assetManager.dispose();
		worldManager.dispose();
		tileMap.dispose();
		tmr.dispose();

		EventManager.get_instance().deregister(PingAssetsEvent.class, this);
		EventManager.get_instance().deregister(ToggleMapRenderEvent.class, this);
		EventManager.get_instance().deregister(ChangeLevelEvent.class, this);
		EventManager.get_instance().deregister(ChangeMapEvent.class, this);
	}
	
	private void loadAssets(AssetManager assets, LevelData levelData)
	{
		for (String image : levelData.getImages())
		{
			assets.load(image, Texture.class);
		}
		assets.finishLoading();
	}

	public void changeLevel(LevelData levelData)
	{
		assetManager.clear();
		loadAssets(assetManager, levelData);

		changeMap(levelData.getMapLocation());

		worldManager.dispose();
		worldManager.loadLevelData(levelData);
	}

	public void changeMap(String mapLocation)
	{
		tileMap.dispose();
		tileMap = new TmxMapLoader().load(mapLocation);
		mapManager.setMap(tileMap);
		tmr.setMap(tileMap);
		EventManager.get_instance().broadcast(new ChangedMapEvent());
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

		EventManager.get_instance().broadcast(new MousePressedEvent(button, screenX, screenY));
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
		if (mouseLeft)
		{
			EventManager.get_instance().broadcast(new MouseDraggedEvent(0, screenX, screenY));
		}
		else if (mouseRight)
		{
			EventManager.get_instance().broadcast(new MouseDraggedEvent(1, screenX, screenY));
		}
		else if (mouseMiddle)
		{
			EventManager.get_instance().broadcast(new MouseDraggedEvent(2, screenX, screenY));
		}
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

	public void handleChangeLevel(ChangeLevelEvent event)
	{
		Json json = new Json();
		JsonTags jsonTags = json.fromJson(JsonTags.class, Gdx.files.internal("data/tags.json"));
		jsonTags.addTags(json);
		LevelData levelData = json.fromJson(LevelData.class, Gdx.files.internal(event.getLevelDataLocation()));
		changeLevel(levelData);
	}

	public void handleChangeMap(ChangeMapEvent event)
	{
		changeMap(event.getNewMapLocation());
	}

}
