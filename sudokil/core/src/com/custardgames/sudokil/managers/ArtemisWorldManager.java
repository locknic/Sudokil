package com.custardgames.sudokil.managers;

import java.util.EventListener;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.systems.ActivityBlockingSystem;
import com.custardgames.sudokil.entities.ecs.systems.ActivitySpriteSystem;
import com.custardgames.sudokil.entities.ecs.systems.CameraMovementSystem;
import com.custardgames.sudokil.entities.ecs.systems.CharacterMovementSystem;
import com.custardgames.sudokil.entities.ecs.systems.DoorToggleSystem;
import com.custardgames.sudokil.entities.ecs.systems.EntityLocatorSystem;
import com.custardgames.sudokil.entities.ecs.systems.EventTriggerSystem;
import com.custardgames.sudokil.entities.ecs.systems.LiftSystem;
import com.custardgames.sudokil.entities.ecs.systems.PowerConsumptionSystem;
import com.custardgames.sudokil.entities.ecs.systems.ProcessQueueSystem;
import com.custardgames.sudokil.entities.ecs.systems.SpriteRenderSystem;
import com.custardgames.sudokil.entities.ecs.systems.TextRenderSystem;
import com.custardgames.sudokil.entities.ecs.systems.UpdatePhysicalCharacterInputSystem;
import com.custardgames.sudokil.entities.ecs.systems.WiredConnectionSystem;
import com.custardgames.sudokil.events.entities.CreateEntityEvent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.utils.EntityHolder;

public class ArtemisWorldManager implements EventListener	
{
	private World artemisWorld;
	
	private SpriteRenderSystem spriteRenderSystem;
	private TextRenderSystem textRenderSystem;
	
	public ArtemisWorldManager(Camera camera, AssetManager assetManager)
	{
		EventManager.get_instance().register(CreateEntityEvent.class, this);
		
		spriteRenderSystem = new SpriteRenderSystem();
		textRenderSystem = new TextRenderSystem();
		
		WorldConfiguration config = new WorldConfigurationBuilder()
				.with(spriteRenderSystem, textRenderSystem, new CharacterMovementSystem(), new CameraMovementSystem(), new UpdatePhysicalCharacterInputSystem(),
						new ProcessQueueSystem(), new EntityLocatorSystem(), new DoorToggleSystem(), new WiredConnectionSystem(), new LiftSystem(),
						new ActivityBlockingSystem(), new PowerConsumptionSystem(), new ActivitySpriteSystem(), new EventTriggerSystem())
				.build().register(camera).register(assetManager);
		artemisWorld = new com.artemis.World(config);

		
		createEntitiesFromJson("maps/campaign/level1/robots.json");
		createEntitiesFromJson("maps/campaign/level1/entities.json");
		createEntitiesFromJson("maps/campaign/level1/triggers.json");
	}
	
	public void update(float dt)
	{
		artemisWorld.setDelta(dt);
		artemisWorld.process();
	}

	public void render(Batch spriteBatch)
	{
		spriteBatch.begin();
		spriteRenderSystem.render(spriteBatch);
		textRenderSystem.render(spriteBatch);
		spriteBatch.end();
	}
	
	public void createEntity(Array<Component> components)
	{
		Entity entity = artemisWorld.createEntity();
		boolean addToMap = false;
		for (Component component : components)
		{
			entity.edit().add(component);
			if (component instanceof PositionComponent)
			{
				addToMap = true;
			}
		}
		artemisWorld.process();
		if (addToMap)
		{
			EventManager.get_instance().broadcast(new AddToMapEvent(entity));
		}
	}
	
	public void createEntitiesFromJson(String fileLocation)
	{
		Json json = new Json();
		EntityHolder componentFactory = json.fromJson(EntityHolder.class, Gdx.files.internal(fileLocation));
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

}
