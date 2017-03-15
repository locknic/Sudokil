package com.custardgames.sudokil.entities.ecs.systems.entities.camera;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.custardgames.sudokil.entities.ecs.components.CameraComponent;
import com.custardgames.sudokil.entities.ecs.components.CameraInputComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.PingEntityEvent;
import com.custardgames.sudokil.events.entities.camera.CameraMovedEvent;
import com.custardgames.sudokil.events.entities.camera.CameraZoomedEvent;
import com.custardgames.sudokil.events.entities.commands.camera.CameraResetEvent;
import com.custardgames.sudokil.events.entities.commands.camera.CameraTargetEvent;
import com.custardgames.sudokil.events.physicalinput.KeyPressedEvent;
import com.custardgames.sudokil.events.physicalinput.KeyReleasedEvent;
import com.custardgames.sudokil.events.physicalinput.MouseDraggedEvent;
import com.custardgames.sudokil.events.physicalinput.MousePressedEvent;
import com.custardgames.sudokil.events.physicalinput.MouseReleasedEvent;
import com.custardgames.sudokil.events.physicalinput.MouseWheelMovedEvent;
import com.custardgames.sudokil.events.ui.ToggleMapRenderEvent;
import com.custardgames.sudokil.managers.EventManager;

public class CameraMovementSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<CameraComponent> cameraComponents;
	private ComponentMapper<CameraInputComponent> cameraInputComponents;
	private ComponentMapper<EntityComponent> entityComponents;

	@Wire
	private OrthographicCamera camera;

	public CameraMovementSystem()
	{
		super(Aspect.all(CameraComponent.class, EntityComponent.class));

		EventManager.get_instance().register(KeyPressedEvent.class, this);
		EventManager.get_instance().register(KeyReleasedEvent.class, this);
		EventManager.get_instance().register(CameraResetEvent.class, this);
		EventManager.get_instance().register(CameraTargetEvent.class, this);
		EventManager.get_instance().register(ToggleMapRenderEvent.class, this);
		EventManager.get_instance().register(MousePressedEvent.class, this);
		EventManager.get_instance().register(MouseReleasedEvent.class, this);
		EventManager.get_instance().register(MouseDraggedEvent.class, this);
		EventManager.get_instance().register(MouseWheelMovedEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(KeyPressedEvent.class, this);
		EventManager.get_instance().deregister(KeyReleasedEvent.class, this);
		EventManager.get_instance().deregister(CameraResetEvent.class, this);
		EventManager.get_instance().deregister(CameraTargetEvent.class, this);
		EventManager.get_instance().deregister(ToggleMapRenderEvent.class, this);
		EventManager.get_instance().deregister(MousePressedEvent.class, this);
		EventManager.get_instance().deregister(MouseReleasedEvent.class, this);
		EventManager.get_instance().deregister(MouseDraggedEvent.class, this);
		EventManager.get_instance().deregister(MouseWheelMovedEvent.class, this);
	}

	@Override
	protected void process(Entity e)
	{
		CameraComponent cameraComponent = cameraComponents.get(e);
		CameraInputComponent cameraInputComponent = cameraInputComponents.get(e);

		float targetX = cameraComponent.getTargetX();
		float targetY = cameraComponent.getTargetY();

		if (cameraComponent.getTargetID() != null && !cameraComponent.getTargetID().equals(""))
		{
			PingEntityEvent event = (PingEntityEvent) EventManager.get_instance().broadcastInquiry(new PingEntityEvent(cameraComponent.getTargetID()));
			if (event != null && event instanceof PingEntityEvent)
			{
				cameraComponent.setTarget(event.getEntity().getComponent(PositionComponent.class));
			}
			cameraComponent.setTargetID(null);
		}

		if (cameraComponent.getTarget() != null)
		{
			if (cameraComponent.getTarget() instanceof PositionComponent)
			{
				targetX = ((PositionComponent) cameraComponent.getTarget()).getX();
				targetY = ((PositionComponent) cameraComponent.getTarget()).getY();
			}
		}

		float deltaX = (float) ((targetX - camera.position.x) * 0.1 + cameraComponent.getTargetOffsetX());
		float deltaY = (float) ((targetY - camera.position.y) * 0.1 + cameraComponent.getTargetOffsetY());
		if (Math.abs(deltaX) > 0.1 || Math.abs(deltaY) > 0.1)
		{
			if (camera.position.x + deltaX <= cameraComponent.getMaxX())
			{
				if (camera.position.x + deltaX >= cameraComponent.getMinX())
				{
					camera.position.x += deltaX;
				}
				else
				{
					camera.position.x = cameraComponent.getMinX();
				}
			}
			else
			{
				camera.position.x = cameraComponent.getMaxX();
			}
			if (camera.position.y + deltaY <= cameraComponent.getMaxY())
			{
				if (camera.position.y + deltaY >= cameraComponent.getMinY())
				{
					camera.position.y += deltaY;
				}
				else
				{
					camera.position.y = cameraComponent.getMinY();
				}
			}
			else
			{
				camera.position.y = cameraComponent.getMaxY();
			}
		}

		if (cameraInputComponent != null)
		{
			if (cameraInputComponent.isUp())
			{
				cameraComponent.setTargetOffsetY((float) (cameraComponent.getTargetOffsetY() + (cameraComponent.getPanSpeed() * camera.zoom)));
			}
			else if (cameraInputComponent.isDown())
			{
				cameraComponent.setTargetOffsetY((float) (cameraComponent.getTargetOffsetY() - (cameraComponent.getPanSpeed() * camera.zoom)));
			}

			if (cameraInputComponent.isLeft())
			{
				cameraComponent.setTargetOffsetX((float) (cameraComponent.getTargetOffsetX() - (cameraComponent.getPanSpeed() * camera.zoom)));
			}
			else if (cameraInputComponent.isRight())
			{
				cameraComponent.setTargetOffsetX((float) (cameraComponent.getTargetOffsetX() + (cameraComponent.getPanSpeed() * camera.zoom)));
			}

			double zoomSpeed = 0.03;

			if (cameraInputComponent.isZoomIn())
			{
				camera.zoom += zoomSpeed;
				EventManager.get_instance().broadcast(new CameraZoomedEvent((float) zoomSpeed));
			}
			else if (cameraInputComponent.isZoomOut() && camera.zoom > zoomSpeed)
			{
				camera.zoom -= zoomSpeed;
				EventManager.get_instance().broadcast(new CameraZoomedEvent((float) -zoomSpeed));
			}

			if (cameraComponent.getZoomAmount() != 0)
			{
				camera.zoom += cameraComponent.getZoomAmount() / 10;
				cameraComponent.setZoomAmount(0);
			}

			if (camera.zoom <= cameraComponent.getMinZoom())
			{
				camera.zoom = cameraComponent.getMinZoom();
			}
			else if (camera.zoom >= cameraComponent.getMaxZoom())
			{
				camera.zoom = cameraComponent.getMaxZoom();
			}

			if (cameraInputComponent.isReset())
			{
				cameraComponent.setTargetOffsetX(0);
				cameraComponent.setTargetOffsetY(0);
				cameraComponent.setTargetX(0);
				cameraComponent.setTargetY(0);
				camera.position.x = 0;
				camera.position.y = 0;
				camera.zoom = 0.5f;
				cameraInputComponent.setReset(false);
			}
		}

		camera.update();
	}

	public void handleKeyPressed(KeyPressedEvent keyEvent)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			CameraInputComponent cameraInput = cameraInputComponents.get(entities.get(x));

			if (cameraInput != null)
			{
				if (keyEvent.getKeyCode() == 152 || keyEvent.getKeyCode() == 19)
				{
					cameraInput.setUp(true);
				}
				else if (keyEvent.getKeyCode() == 146 || keyEvent.getKeyCode() == 20)
				{
					cameraInput.setDown(true);
				}

				if (keyEvent.getKeyCode() == 148 || keyEvent.getKeyCode() == 21)
				{
					cameraInput.setLeft(true);
				}
				else if (keyEvent.getKeyCode() == 150 || keyEvent.getKeyCode() == 22)
				{
					cameraInput.setRight(true);
				}

				if (keyEvent.getKeyCode() == 151 || keyEvent.getKeyCode() == 69)
				{
					cameraInput.setZoomIn(true);
				}
				else if (keyEvent.getKeyCode() == 153 || keyEvent.getKeyCode() == 70)
				{
					cameraInput.setZoomOut(true);
				}

				if (keyEvent.getKeyCode() == 149 || keyEvent.getKeyCode() == 7)
				{
					cameraInput.setReset(true);
				}
			}
		}
	}

	public void handleKeyReleased(KeyReleasedEvent keyEvent)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			CameraInputComponent cameraInput = cameraInputComponents.get(entities.get(x));

			if (cameraInput != null)
			{
				if (keyEvent.getKeyCode() == 152 || keyEvent.getKeyCode() == 19)
				{
					cameraInput.setUp(false);
				}
				else if (keyEvent.getKeyCode() == 146 || keyEvent.getKeyCode() == 20)
				{
					cameraInput.setDown(false);
				}

				if (keyEvent.getKeyCode() == 148 || keyEvent.getKeyCode() == 21)
				{
					cameraInput.setLeft(false);
				}
				else if (keyEvent.getKeyCode() == 150 || keyEvent.getKeyCode() == 22)
				{
					cameraInput.setRight(false);
				}

				if (keyEvent.getKeyCode() == 151 || keyEvent.getKeyCode() == 69)
				{
					cameraInput.setZoomIn(false);
				}
				else if (keyEvent.getKeyCode() == 153 || keyEvent.getKeyCode() == 70)
				{
					cameraInput.setZoomOut(false);
				}
			}
		}
	}

	public void handleCameraTargetEvent(CameraTargetEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			EntityComponent entityComponent = entityComponents.get(entity);
			CameraComponent cameraInput = cameraComponents.get(entity);

			if (entityComponent.getId().equals(event.getEntityName()))
			{
				cameraInput.setTargetID(event.getTargetEntity());
				cameraInput.setTargetOffsetX(0);
				cameraInput.setTargetOffsetY(0);
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

			if (cameraInput != null && entityComponent.getId().equals(event.getEntityName()))
			{
				cameraInput.setReset(true);
			}
		}
	}

	public void handleToggleMapRender(ToggleMapRenderEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			CameraInputComponent cameraInput = cameraInputComponents.get(entities.get(x));
			if (cameraInput != null)
			{
				cameraInput.setReset(true);
			}
		}
	}

	public void handleMousePressed(MousePressedEvent event)
	{
		if (event.getButtonNumber() == 0)
		{

			ImmutableBag<Entity> entities = getEntities();
			for (int x = 0; x < entities.size(); x++)
			{
				CameraInputComponent cameraInput = cameraInputComponents.get(entities.get(x));
				if (cameraInput != null)
				{
					cameraInput.setMousePressing(true);
					cameraInput.setMouseX(event.getX());
					cameraInput.setMouseY(event.getY());
				}
			}
		}
	}

	public void handleMouseReleased(MouseReleasedEvent event)
	{
		if (event.getButtonNumber() == 0)
		{
			ImmutableBag<Entity> entities = getEntities();
			for (int x = 0; x < entities.size(); x++)
			{
				CameraInputComponent cameraInput = cameraInputComponents.get(entities.get(x));
				if (cameraInput != null)
				{
					cameraInput.setMousePressing(false);
					cameraInput.setMouseX(0);
					cameraInput.setMouseY(0);
					EventManager.get_instance().broadcast(new CameraMovedEvent());
				}
			}
		}
	}

	public void handleMouseDragged(MouseDraggedEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			CameraComponent cameraComponent = cameraComponents.get(entities.get(x));
			CameraInputComponent cameraInput = cameraInputComponents.get(entities.get(x));
			if (cameraInput != null)
			{
				if (event.getButton() == 0)
				{
					cameraComponent.setTargetX(cameraComponent.getTargetX() - ((event.getX() - cameraInput.getMouseX()) * camera.zoom));
					cameraComponent.setTargetY(cameraComponent.getTargetY() + ((event.getY() - cameraInput.getMouseY()) * camera.zoom));
					cameraInput.setMouseX(event.getX());
					cameraInput.setMouseY(event.getY());
				}
			}
		}
	}

	public void handleMouseWheelMoved(MouseWheelMovedEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			CameraComponent cameraInput = cameraComponents.get(entities.get(x));
			cameraInput.setZoomAmount(event.getMouseWheelAmount());
			EventManager.get_instance().broadcast(new CameraZoomedEvent(event.getMouseWheelAmount()));
		}
	}

}
