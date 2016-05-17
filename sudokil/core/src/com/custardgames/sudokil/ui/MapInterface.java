package com.custardgames.sudokil.ui;

import java.util.EventListener;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.entities.ecs.factories.EntityFactoryJSON;
import com.custardgames.sudokil.entities.ecs.systems.ActivityBlockingSystem;
import com.custardgames.sudokil.entities.ecs.systems.ActivitySpriteSystem;
import com.custardgames.sudokil.entities.ecs.systems.CameraMovementSystem;
import com.custardgames.sudokil.entities.ecs.systems.CharacterMovementSystem;
import com.custardgames.sudokil.entities.ecs.systems.DoorToggleSystem;
import com.custardgames.sudokil.entities.ecs.systems.EntityLocatorSystem;
import com.custardgames.sudokil.entities.ecs.systems.LiftSystem;
import com.custardgames.sudokil.entities.ecs.systems.PowerConsumptionSystem;
import com.custardgames.sudokil.entities.ecs.systems.ProcessQueueSystem;
import com.custardgames.sudokil.entities.ecs.systems.SpriteRenderSystem;
import com.custardgames.sudokil.entities.ecs.systems.TextRenderSystem;
import com.custardgames.sudokil.entities.ecs.systems.UpdatePhysicalCharacterInputSystem;
import com.custardgames.sudokil.entities.ecs.systems.WiredConnectionSystem;
import com.custardgames.sudokil.events.PingAssetsEvent;
import com.custardgames.sudokil.events.physicalinput.KeyPressedEvent;
import com.custardgames.sudokil.events.physicalinput.KeyReleasedEvent;
import com.custardgames.sudokil.events.physicalinput.MousePressedEvent;
import com.custardgames.sudokil.events.physicalinput.MouseReleasedEvent;
import com.custardgames.sudokil.events.physicalinput.MouseWheelMovedEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.managers.InputManager;
import com.custardgames.sudokil.managers.MapManager;
import com.custardgames.sudokil.states.PlayLoadAssets;

public class MapInterface extends Stage implements EventListener
{
	private AssetManager assetManager;
	private com.artemis.World artemisWorld;

	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	private OrthographicCamera camera;
	private SpriteRenderSystem spriteRenderSystem;
	private TextRenderSystem textRenderSystem;
	private Actor map;

	private Array<String> kcInput;

	private int mouseX, mouseY, mouseWheelRotation;
	private boolean mouseLeft, mouseRight, mouseMiddle;

	public MapInterface()
	{
		init();
	}

	public void init()
	{
		InputManager.get_instance().addProcessor(this);
		EventManager.get_instance().register(PingAssetsEvent.class, this);

		kcInput = new Array<String>();
		mouseX = mouseY = mouseWheelRotation = 0;
		mouseLeft = mouseRight = mouseMiddle = false;

		assetManager = new PlayLoadAssets().loadAssets(new AssetManager());

		spriteRenderSystem = new SpriteRenderSystem();
		textRenderSystem = new TextRenderSystem();

		tileMap = new TmxMapLoader().load("maps/test.tmx");
		new MapManager(tileMap);
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		camera = new OrthographicCamera();
		tmr.setView(camera);
		this.getViewport().setCamera(camera);

		WorldConfiguration config = new WorldConfigurationBuilder().with(spriteRenderSystem, textRenderSystem, new CharacterMovementSystem(), new CameraMovementSystem(), new UpdatePhysicalCharacterInputSystem(),
				new ProcessQueueSystem(), new EntityLocatorSystem(), new DoorToggleSystem(), new WiredConnectionSystem(), new LiftSystem(), new ActivityBlockingSystem(), new PowerConsumptionSystem(),
				new ActivitySpriteSystem()).build().register(camera).register(assetManager);
		artemisWorld = new com.artemis.World(config);
		
		EntityFactoryJSON entityFactory = new EntityFactoryJSON(artemisWorld);
		entityFactory.createEntities("maps/campaign/level1/player.json");

		map = new Actor();
		map.setSize(camera.viewportWidth, camera.viewportHeight);
		this.addActor(map);

	}

	public void update(float dt)
	{
		artemisWorld.setDelta(dt);
		artemisWorld.process();
		this.act(dt);
	}

	public void render()
	{
		camera.update();
		tmr.setView(camera);
		tmr.render();
		Batch spriteBatch = getBatch();
		spriteBatch.begin();
		spriteRenderSystem.render(spriteBatch);
		textRenderSystem.render(spriteBatch);
		spriteBatch.end();
		this.draw();
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
			// System.out.println("Adding: " + keycode);
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
			// System.out.println("Removing: " + keycode);
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

}
