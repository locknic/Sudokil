package com.custardgames.sudokil.entities.ecs.systems.rendering;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
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

	private ShapeRenderer shapeRenderer;

	private Array<Entity> selectedEntities;
	private Array<Entity> possibleSelectedEntities;

	@SuppressWarnings("unchecked")
	public ConsoleHighlightRenderSystem()
	{
		super(Aspect.all(PositionComponent.class));

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
		shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
		Gdx.gl.glDisable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Line);

		shapeRenderer.setColor(Color.GREEN);
		for (Entity entity : selectedEntities)
		{
			PositionComponent positionComponent = positionComponents.get(entity);
			if (positionComponent != null)
			{
				shapeRenderer.rect(positionComponent.getX() + 2, positionComponent.getY() + 2, positionComponent.getWidth() - 4,
						positionComponent.getHeight() - 4);
			}
		}

		shapeRenderer.setColor(Color.ORANGE);
		for (Entity entity : possibleSelectedEntities)
		{
			PositionComponent positionComponent = positionComponents.get(entity);
			if (positionComponent != null)
			{
				shapeRenderer.rect(positionComponent.getX() + 2, positionComponent.getY() + 2, positionComponent.getWidth() - 4,
						positionComponent.getHeight() - 4);
			}
		}

		shapeRenderer.end();
		spriteBatch.begin();
	}

	public void handleHighlight(HighlightEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
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
