package com.custardgames.sudokil.managers;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.custardgames.sudokil.entities.ecs.components.Box2DBodyComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.lights.ConeLightComponent;
import com.custardgames.sudokil.entities.ecs.components.lights.PointLightComponent;
import com.custardgames.sudokil.entities.ecs.systems.ActivityBlockingSystem;
import com.custardgames.sudokil.entities.ecs.systems.ActivitySpriteSystem;
import com.custardgames.sudokil.entities.ecs.systems.CharacterInputSystem;
import com.custardgames.sudokil.entities.ecs.systems.EntityLocatorSystem;
import com.custardgames.sudokil.entities.ecs.systems.EventTriggerSystem;
import com.custardgames.sudokil.entities.ecs.systems.MapRefresherSystem;
import com.custardgames.sudokil.entities.ecs.systems.ProcessQueueSystem;
import com.custardgames.sudokil.entities.ecs.systems.current.CurrentConsumptionSystem;
import com.custardgames.sudokil.entities.ecs.systems.current.CurrentToggleSystem;
import com.custardgames.sudokil.entities.ecs.systems.entities.ComputerSpriteSystem;
import com.custardgames.sudokil.entities.ecs.systems.entities.DoorToggleSystem;
import com.custardgames.sudokil.entities.ecs.systems.entities.GeneratorSpriteSystem;
import com.custardgames.sudokil.entities.ecs.systems.entities.LampSpriteSystem;
import com.custardgames.sudokil.entities.ecs.systems.entities.PowerCellSpriteSystem;
import com.custardgames.sudokil.entities.ecs.systems.entities.camera.CameraMovementSystem;
import com.custardgames.sudokil.entities.ecs.systems.entities.robot.RobotConnectSystem;
import com.custardgames.sudokil.entities.ecs.systems.entities.robot.RobotLiftSystem;
import com.custardgames.sudokil.entities.ecs.systems.entities.robot.RobotMovementSystem;
import com.custardgames.sudokil.entities.ecs.systems.entities.robot.RobotSpriteSystem;
import com.custardgames.sudokil.entities.ecs.systems.network.NetworkSystem;
import com.custardgames.sudokil.entities.ecs.systems.network.NetworksConnectedSystem;
import com.custardgames.sudokil.entities.ecs.systems.power.PowerConsumptionSystem;
import com.custardgames.sudokil.entities.ecs.systems.power.PowerLightSystem;
import com.custardgames.sudokil.entities.ecs.systems.rendering.ConsoleHighlightRenderSystem;
import com.custardgames.sudokil.entities.ecs.systems.rendering.ShapeRenderSystem;
import com.custardgames.sudokil.entities.ecs.systems.rendering.SpriteRenderSystem;
import com.custardgames.sudokil.entities.ecs.systems.rendering.TextRenderSystem;
import com.custardgames.sudokil.entities.ecs.systems.rendering.WireRenderSystem;
import com.custardgames.sudokil.events.AddEntitiesEvent;
import com.custardgames.sudokil.events.DisposeWorldEvent;
import com.custardgames.sudokil.events.MapEntitiesLoadedEvent;
import com.custardgames.sudokil.events.entities.CreateEntityBox2DBodyEvent;
import com.custardgames.sudokil.events.entities.CreateEntityEvent;
import com.custardgames.sudokil.events.entities.CreateEntityLightEvent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.utils.EntityHolder;
import com.custardgames.sudokil.utils.EntityMapLoader;
import com.custardgames.sudokil.utils.JsonTags;
import com.custardgames.sudokil.utils.LevelData;

public class ArtemisWorldManager implements EventListener
{
	private World artemisWorld;
	
	private WireRenderSystem wireRenderSystem;
	private SpriteRenderSystem spriteRenderSystem;
	private ShapeRenderSystem shapeRenderSystem;
	private TextRenderSystem textRenderSystem;
	private ConsoleHighlightRenderSystem consoleHighlightRenderSystem;

	public ArtemisWorldManager(Camera camera, CustardAssetManager assetManager, LevelData levelData)
	{
		EventManager.get_instance().register(CreateEntityEvent.class, this);
		EventManager.get_instance().register(AddEntitiesEvent.class, this);

		wireRenderSystem = new WireRenderSystem();
		spriteRenderSystem = new SpriteRenderSystem();
		shapeRenderSystem = new ShapeRenderSystem();
		textRenderSystem = new TextRenderSystem();
		consoleHighlightRenderSystem = new ConsoleHighlightRenderSystem();

		WorldConfiguration config = new WorldConfigurationBuilder().with(wireRenderSystem, spriteRenderSystem, shapeRenderSystem, textRenderSystem, consoleHighlightRenderSystem,
				new RobotMovementSystem(), new CameraMovementSystem(), new CharacterInputSystem(), new ProcessQueueSystem(), new EntityLocatorSystem(),
				new DoorToggleSystem(), new RobotConnectSystem(), new RobotLiftSystem(), new ActivityBlockingSystem(), new PowerConsumptionSystem(),
				new ActivitySpriteSystem(), new EventTriggerSystem(), new MapRefresherSystem(), new NetworkSystem(), new NetworksConnectedSystem(),
				new CurrentConsumptionSystem(), new CurrentToggleSystem(), new RobotSpriteSystem(), new PowerLightSystem(), new LampSpriteSystem(),
				new PowerCellSpriteSystem(), new ComputerSpriteSystem(), new GeneratorSpriteSystem()).build().register(camera).register(assetManager);
		artemisWorld = new com.artemis.World(config);
		loadLevelData(levelData);
	}

	public void loadMapEntities(TiledMap map)
	{
		EntityHolder entities = EntityMapLoader.createEntityHolderJsonFromMap(map);
		createEntitiesFromFactory(entities);
		EventManager.get_instance().broadcast(new MapEntitiesLoadedEvent());
	}

	public void loadLevelData(LevelData levelData)
	{
		for (String entityLocation : levelData.getEntities())
		{
			createEntitiesFromJson(entityLocation);
		}
	}

	public void dispose()
	{
		EventManager.get_instance().broadcast(new DisposeWorldEvent());

		IntBag entities = artemisWorld.getAspectSubscriptionManager().get(Aspect.all()).getEntities();

		int[] ids = entities.getData();
		for (int i = 0, s = entities.size(); s > i; i++)
		{
			artemisWorld.delete(ids[i]);
		}
	}

	public void update(float dt)
	{
		artemisWorld.setDelta(dt);
		artemisWorld.process();
	}

	public void render(Batch spriteBatch)
	{
		spriteBatch.begin();
		shapeRenderSystem.render(spriteBatch);
		consoleHighlightRenderSystem.render(spriteBatch);
		wireRenderSystem.render(spriteBatch);
		spriteRenderSystem.render(spriteBatch);
		textRenderSystem.render(spriteBatch);
		spriteBatch.end();
	}

	public void createEntity(Array<Component> components)
	{
		Entity entity = artemisWorld.createEntity();

		PositionComponent positionComponent = null;
		Box2DBodyComponent bodyComponent = null;
		PointLightComponent pointLightComponent = null;
		ConeLightComponent coneLightComponent = null;

		for (Component component : components)
		{
			entity.edit().add(component);
			if (component instanceof PositionComponent)
			{
				positionComponent = (PositionComponent) component;
			}
			else if (component instanceof Box2DBodyComponent)
			{
				bodyComponent = (Box2DBodyComponent) component;
			}
			else if (component instanceof PointLightComponent)
			{
				pointLightComponent = (PointLightComponent) component;
			}
			else if (component instanceof ConeLightComponent)
			{
				coneLightComponent = (ConeLightComponent) component;
			}
		}
		artemisWorld.process();

		if (positionComponent != null)
		{
			positionComponent.setExpectedX(positionComponent.getX());
			positionComponent.setExpectedY(positionComponent.getY());
			EventManager.get_instance().broadcast(new AddToMapEvent(entity));

			if (bodyComponent != null)
			{
				EventManager.get_instance().broadcast(new CreateEntityBox2DBodyEvent(positionComponent, bodyComponent));
			}
		}
		if (pointLightComponent != null)
		{
			EventManager.get_instance().broadcast(new CreateEntityLightEvent(pointLightComponent, bodyComponent));
		}
		if (coneLightComponent != null)
		{
			EventManager.get_instance().broadcast(new CreateEntityLightEvent(coneLightComponent, bodyComponent));
		}
	}

	public void createEntitiesFromJson(String fileLocation)
	{
		Json json = new Json();
		JsonTags jsonTags = json.fromJson(JsonTags.class, Gdx.files.internal("data/tags.json"));
		jsonTags.addTags(json);
		EntityHolder componentFactory = json.fromJson(EntityHolder.class, Gdx.files.internal(fileLocation));
		createEntitiesFromFactory(componentFactory);
	}

	public void createEntitiesFromFactory(EntityHolder componentFactory)
	{
		Array<Array<Component>> entities = componentFactory.getComponents();
		for (Array<Component> components : entities)
		{
			createEntity(components);
		}
	}

	public void handleCreateEntity(CreateEntityEvent event)
	{
		createEntity(event.getComponents());
	}

	public void handleAddEntities(AddEntitiesEvent event)
	{
		createEntitiesFromJson(event.getEntitiesLocation());
	}

}
