package com.custardgames.sudokil.managers;

import java.util.EventListener;
import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.entities.ecs.components.Box2DBodyComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.lights.ConeLightComponent;
import com.custardgames.sudokil.entities.ecs.components.lights.LightComponent;
import com.custardgames.sudokil.entities.ecs.components.lights.PointLightComponent;
import com.custardgames.sudokil.events.entities.CreateEntityBox2DBodyEvent;
import com.custardgames.sudokil.events.entities.CreateEntityLightEvent;
import com.custardgames.sudokil.events.entities.EntityMovedEvent;
import com.custardgames.sudokil.events.entities.EntityTurnedEvent;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

public class Box2dWorldManager implements EventListener
{
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private Box2DMapObjectParser mapParser;
	private RayHandler rayHandler;
	private final boolean debugMode = false;

	public Box2dWorldManager()
	{
		EventManager.get_instance().register(CreateEntityBox2DBodyEvent.class, this);
		EventManager.get_instance().register(EntityMovedEvent.class, this);
		EventManager.get_instance().register(EntityTurnedEvent.class, this);
		EventManager.get_instance().register(CreateEntityLightEvent.class, this);

		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		mapParser = new Box2DMapObjectParser();
		rayHandler = new RayHandler(world);
		RayHandler.useDiffuseLight(true);
		rayHandler.setAmbientLight(0.9f, 0.9f, 0.9f, 1f);
	}

	public void dispose()
	{
		EventManager.get_instance().deregister(CreateEntityBox2DBodyEvent.class, this);
		EventManager.get_instance().deregister(EntityMovedEvent.class, this);
		EventManager.get_instance().deregister(EntityTurnedEvent.class, this);
		EventManager.get_instance().deregister(CreateEntityLightEvent.class, this);

		rayHandler.dispose();
		world.dispose();
	}

	public World getWorld()
	{
		return world;
	}

	public void loadMap(TiledMap map)
	{
		mapParser.load(world, map);
	}

	public void update(float delta)
	{
		// CHANGE THIS IF THE GAME NEEDS TO START SIMULATING PHYSICS
		// CURRENTLY ONLY USING 1 STEP PER TICK BUT SHOULD USE MORE FOR SIMULATION
		world.step(delta, 1, 1);
	}

	public void render(OrthographicCamera camera)
	{
		rayHandler.setCombinedMatrix(camera);
		rayHandler.updateAndRender();
		if (debugMode)
		{
			debugRenderer.render(world, camera.combined);
		}
	}

	public void clear()
	{
		rayHandler.removeAll();
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		Iterator<Body> bodiesIterator = bodies.iterator();
		while(bodiesIterator.hasNext())
		{
			bodiesIterator.next();
			bodiesIterator.remove();
		}
		
		mapParser = new Box2DMapObjectParser();
	}

	public void clearMapObjects()
	{
		if (world != null && world.getBodyCount() > 0)
		{
			Iterator<Body> bodies = mapParser.getBodies().values().iterator();

			while (bodies.hasNext())
			{
				world.destroyBody(bodies.next());
			}

			mapParser = new Box2DMapObjectParser();
		}
	}
	
	public void createEntityBody(PositionComponent positionComponent, Box2DBodyComponent bodyComponent)
	{
		if (bodyComponent != null && positionComponent != null)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			bodyDef.position.set(positionComponent.getX() + positionComponent.getWidth() / 2, positionComponent.getY() + positionComponent.getHeight() / 2);
			bodyDef.angle = (float) Math.toRadians(positionComponent.getAngle());

			Body body = world.createBody(bodyDef);
			PolygonShape rect = new PolygonShape();
			rect.setAsBox(positionComponent.getWidth() / 2, positionComponent.getHeight() / 2, body.getLocalCenter(), 0);

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = rect;
			fixtureDef.density = 0.5f;
			fixtureDef.friction = 0.4f;
			fixtureDef.restitution = 0.6f;

			body.createFixture(fixtureDef);
			rect.dispose();

			bodyComponent.setBody(body);
		}
	}

	public void createEntityLight(LightComponent lightComponent, Box2DBodyComponent bodyComponent)
	{
		if (lightComponent != null)
		{
			if (lightComponent instanceof ConeLightComponent)
			{
				ConeLight coneLight = new ConeLight(rayHandler, 64, lightComponent.getColor(), lightComponent.getDistance(), lightComponent.getxCo(),
						lightComponent.getyCo(), ((ConeLightComponent) lightComponent).getDirectionDegree(),
						((ConeLightComponent) lightComponent).getConeDegree());
				
				lightComponent.setLight(coneLight);

				if (bodyComponent != null)
				{
					coneLight.attachToBody(bodyComponent.getBody(), lightComponent.getxCo(), lightComponent.getyCo());
				}
			}
			else if (lightComponent instanceof PointLightComponent)
			{
				PointLight pointLight = new PointLight(rayHandler, 64, lightComponent.getColor(), lightComponent.getDistance(), lightComponent.getxCo(),
						lightComponent.getyCo());
				
				lightComponent.setLight(pointLight);
				
				if (bodyComponent != null)
				{
					pointLight.attachToBody(bodyComponent.getBody(), lightComponent.getxCo(), lightComponent.getyCo());
				}
			}
		}
	}

	public void updateBodyData(PositionComponent positionComponent, Box2DBodyComponent bodyComponent)
	{
		if (bodyComponent != null && positionComponent != null && bodyComponent.getBody() != null)
		{
			bodyComponent.getBody().setTransform(positionComponent.getX() + positionComponent.getWidth() / 2,
					positionComponent.getY() + positionComponent.getHeight() / 2, (float) Math.toRadians(positionComponent.getAngle()));
		}
	}

	public void handleCreateEntityBox2DBody(CreateEntityBox2DBodyEvent event)
	{
		createEntityBody(event.getPositionComponent(), event.getBodyComponent());
	}

	public void handleEntityMoved(EntityMovedEvent event)
	{
		Box2DBodyComponent bodyComponent = event.getEntity().getComponent(Box2DBodyComponent.class);
		PositionComponent positionComponent = event.getEntity().getComponent(PositionComponent.class);
		updateBodyData(positionComponent, bodyComponent);
	}

	public void handleEntityTurned(EntityTurnedEvent event)
	{
		Box2DBodyComponent bodyComponent = event.getEntity().getComponent(Box2DBodyComponent.class);
		PositionComponent positionComponent = event.getEntity().getComponent(PositionComponent.class);
		updateBodyData(positionComponent, bodyComponent);
	}

	public void handleCreateEntityLight(CreateEntityLightEvent event)
	{
		createEntityLight(event.getLightComponent(), event.getBodyComponent());
	}
}
