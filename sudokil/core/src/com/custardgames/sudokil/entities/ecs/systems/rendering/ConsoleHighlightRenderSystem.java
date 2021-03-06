package com.custardgames.sudokil.entities.ecs.systems.rendering;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.entities.ecs.components.CameraComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.ChangeLevelEvent;
import com.custardgames.sudokil.events.commandLine.HighlightEvent;
import com.custardgames.sudokil.events.commandLine.ResetHighlightEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ConsoleHighlightRenderSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<PositionComponent> positionComponents;
	private ComponentMapper<EntityComponent> entityComponents;
	private ComponentMapper<CameraComponent> cameraComponents;

	private ShapeRenderer shapeRenderer;

	private Array<Entity> selectedEntities;
	private Array<Entity> possibleSelectedEntities;

	public ConsoleHighlightRenderSystem()
	{
		super(Aspect.all(CameraComponent.class));

		EventManager.get_instance().register(HighlightEvent.class, this);
		EventManager.get_instance().register(ResetHighlightEvent.class, this);
		EventManager.get_instance().register(ChangeLevelEvent.class, this);

		shapeRenderer = new ShapeRenderer();
		selectedEntities = new Array<Entity>();
		possibleSelectedEntities = new Array<Entity>();
	}

	@Override
	public boolean checkProcessing()
	{
		return false;
	}

	@Override
	protected void process(Entity e)
	{

	}

	@Override
	public void dispose()
	{
		super.dispose();
		shapeRenderer.dispose();
		EventManager.get_instance().deregister(HighlightEvent.class, this);
		EventManager.get_instance().deregister(ResetHighlightEvent.class, this);
		EventManager.get_instance().deregister(ChangeLevelEvent.class, this);
	}

	public void render(Batch spriteBatch)
	{
		spriteBatch.end();

		ImmutableBag<Entity> entities = getEntities();
		for (Entity camera : entities)
		{
			CameraComponent cameraComponent = cameraComponents.get(camera);

			if (cameraComponent.isSelected() && cameraComponent.isHighlightEntities())
			{
				shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
				Gdx.gl.glEnable(GL20.GL_BLEND);
				shapeRenderer.begin(ShapeType.Line);

				shapeRenderer.setColor(Color.WHITE);
				for (Entity entity : selectedEntities)
				{
					PositionComponent positionComponent = positionComponents.get(entity);
					if (positionComponent != null)
					{
						shapeRenderer.rect(positionComponent.getExpectedX() + 2, positionComponent.getExpectedY() + 2, positionComponent.getWidth() - 4,
								positionComponent.getHeight() - 4);
					}
				}

				shapeRenderer.getColor().a = 0.3f;
				for (Entity entity : possibleSelectedEntities)
				{
					PositionComponent positionComponent = positionComponents.get(entity);
					if (positionComponent != null)
					{
						shapeRenderer.rect(positionComponent.getExpectedX() + 2, positionComponent.getExpectedY() + 2, positionComponent.getWidth() - 4,
								positionComponent.getHeight() - 4);
					}
				}
				shapeRenderer.getColor().a = 1.0f;

				shapeRenderer.end();
			}
		}

		spriteBatch.begin();
	}

	public void handleHighlight(HighlightEvent event)
	{
		EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(PositionComponent.class));
		IntBag entityIds = subscription.getEntities();
		
		for (int x = 0; x < entityIds.size(); x++)
		{
			Entity entity = world.getEntity(entityIds.get(x));
			EntityComponent entityComponent = entityComponents.get(entity);

			if (entityComponent != null && event.getEntity().equals(entityComponent.getId()))
			{
				if (event.isPossibleSelection())
				{
					possibleSelectedEntities.add(entity);
				}
				else
				{
					selectedEntities.add(entity);
				}
			}
		}
	}

	public void handleResetHighlight(ResetHighlightEvent event)
	{
		selectedEntities.clear();
		possibleSelectedEntities.clear();
	}

	public void handleChangeLevel(ChangeLevelEvent event)
	{
		selectedEntities.clear();
		possibleSelectedEntities.clear();
	}

}
