package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.custardgames.sudokil.entities.ecs.components.CameraInputComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.PingEntityEvent;
import com.custardgames.sudokil.events.entities.commands.camera.CameraResetEvent;
import com.custardgames.sudokil.events.entities.commands.camera.CameraTargetEvent;
import com.custardgames.sudokil.events.physicalinput.KeyPressedEvent;
import com.custardgames.sudokil.events.physicalinput.KeyReleasedEvent;
import com.custardgames.sudokil.managers.EventManager;

public class CameraMovementSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<CameraInputComponent> cameraInputComponents;
	private ComponentMapper<EntityComponent> entityComponents;
	
	@Wire
	private OrthographicCamera camera;

	@SuppressWarnings("unchecked")
	public CameraMovementSystem()
	{
		super(Aspect.all(CameraInputComponent.class, EntityComponent.class));

		EventManager.get_instance().register(KeyPressedEvent.class, this);
		EventManager.get_instance().register(KeyReleasedEvent.class, this);
		EventManager.get_instance().register(CameraResetEvent.class, this);
		EventManager.get_instance().register(CameraTargetEvent.class, this);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		EventManager.get_instance().deregister(KeyPressedEvent.class, this);
		EventManager.get_instance().deregister(KeyReleasedEvent.class, this);
		EventManager.get_instance().deregister(CameraResetEvent.class, this);
		EventManager.get_instance().deregister(CameraTargetEvent.class, this);
	}

	@Override
	protected void process(Entity e)
	{
		CameraInputComponent cameraInput = cameraInputComponents.get(e);

		float targetX = cameraInput.getTargetX();
		float targetY = cameraInput.getTargetY();

		if (cameraInput.getTargetID() != null && !cameraInput.getTargetID().equals(""))
		{
			PingEntityEvent event = (PingEntityEvent) EventManager.get_instance()
					.broadcastInquiry(new PingEntityEvent(cameraInput.getTargetID()));
			if (event != null && event instanceof PingEntityEvent)
			{
				cameraInput.setTarget(event.getEntity().getComponent(PositionComponent.class));
			}
			cameraInput.setTargetID(null);
		}

		if (cameraInput.getTarget() != null)
		{
			if (cameraInput.getTarget() instanceof PositionComponent)
			{
				targetX = ((PositionComponent) cameraInput.getTarget()).getX();
				targetY = ((PositionComponent) cameraInput.getTarget()).getY();
			}
		}

		camera.position.x += (targetX - camera.position.x) * 0.1 + cameraInput.getTargetOffsetX();
		camera.position.y += (targetY - camera.position.y) * 0.1 + cameraInput.getTargetOffsetY();

		if (cameraInput.isUp())
		{
			cameraInput.setTargetOffsetY((float) (cameraInput.getTargetOffsetY() + cameraInput.getPanSpeed()));
		}
		else if (cameraInput.isDown())
		{
			cameraInput.setTargetOffsetY((float) (cameraInput.getTargetOffsetY() - cameraInput.getPanSpeed()));
		}

		if (cameraInput.isLeft())
		{
			cameraInput.setTargetOffsetX((float) (cameraInput.getTargetOffsetX() - cameraInput.getPanSpeed()));
		}
		else if (cameraInput.isRight())
		{
			cameraInput.setTargetOffsetX((float) (cameraInput.getTargetOffsetX() + cameraInput.getPanSpeed()));
		}

		double zoomSpeed = 0.03;

		if (cameraInput.isZoomIn())
		{
			camera.zoom += zoomSpeed;
		}
		else if (cameraInput.isZoomOut() && camera.zoom > zoomSpeed)
		{
			camera.zoom -= zoomSpeed;
		}

		if (cameraInput.isReset())
		{
			cameraInput.setTargetOffsetX(0);
			cameraInput.setTargetOffsetY(0);
			camera.zoom = 1;
			cameraInput.setReset(false);
		}

		camera.update();
	}

	public void handleKeyPressed(KeyPressedEvent keyEvent)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			CameraInputComponent cameraInput = cameraInputComponents.get(entities.get(x));

			if (keyEvent.getKeyCode() == 152)
			{
				cameraInput.setUp(true);
			}
			else if (keyEvent.getKeyCode() == 146)
			{
				cameraInput.setDown(true);
			}

			if (keyEvent.getKeyCode() == 148)
			{
				cameraInput.setLeft(true);
			}
			else if (keyEvent.getKeyCode() == 150)
			{
				cameraInput.setRight(true);
			}

			if (keyEvent.getKeyCode() == 151)
			{
				cameraInput.setZoomIn(true);
			}
			else if (keyEvent.getKeyCode() == 153)
			{
				cameraInput.setZoomOut(true);
			}

			if (keyEvent.getKeyCode() == 149)
			{
				cameraInput.setReset(true);
			}
		}
	}

	public void handleKeyReleased(KeyReleasedEvent keyEvent)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			CameraInputComponent cameraInput = cameraInputComponents.get(entities.get(x));

			if (keyEvent.getKeyCode() == 152)
			{
				cameraInput.setUp(false);
			}
			else if (keyEvent.getKeyCode() == 146)
			{
				cameraInput.setDown(false);
			}

			if (keyEvent.getKeyCode() == 148)
			{
				cameraInput.setLeft(false);
			}
			else if (keyEvent.getKeyCode() == 150)
			{
				cameraInput.setRight(false);
			}

			if (keyEvent.getKeyCode() == 151)
			{
				cameraInput.setZoomIn(false);
			}
			else if (keyEvent.getKeyCode() == 153)
			{
				cameraInput.setZoomOut(false);
			}
		}
	}

	public void handleCameraTargetEvent(CameraTargetEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			EntityComponent entityComponent = entityComponents.get(entity);
			CameraInputComponent cameraInput = cameraInputComponents.get(entity);

			if (entityComponent.getId().equals(event.getEntityName()) && event.getArgs() != null
					&& event.getArgs().length > 1)
			{
				cameraInput.setTargetID(event.getArgs()[1]);
			}
			else
			{
				cameraInput.setTarget(null);
			}
		}
	}

	public void handleCameraResetEvent(CameraResetEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			EntityComponent entityComponent = entityComponents.get(entities.get(x));
			CameraInputComponent cameraInput = cameraInputComponents.get(entities.get(x));

			if (entityComponent.getId().equals(event.getEntityName()))
			{
				cameraInput.setReset(true);
			}
		}
	}

}
