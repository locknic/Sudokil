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
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.ShapeRenderableComponent;
import com.custardgames.sudokil.events.entities.SetShapeRenderEvent;
import com.custardgames.sudokil.managers.EventManager;

public class ShapeRenderSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<ShapeRenderableComponent> shapeComponents;
	private ComponentMapper<PositionComponent> positionComponents;
	private ComponentMapper<EntityComponent> entityComponents;
	
	private ShapeRenderer shapeRenderer;
	
	@SuppressWarnings("unchecked")
	public ShapeRenderSystem()
	{
		super(Aspect.all(ShapeRenderableComponent.class, PositionComponent.class));
		
		EventManager.get_instance().register(SetShapeRenderEvent.class, this);

		shapeRenderer = new ShapeRenderer();
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
		EventManager.get_instance().deregister(SetShapeRenderEvent.class, this);
	}
	
	public void render(Batch spriteBatch)
	{
		spriteBatch.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.getColor().a = 0.3f;
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			ShapeRenderableComponent shapeComponent = shapeComponents.get(entity);
			PositionComponent positionComponent = positionComponents.get(entity);
			if (shapeComponent.isShouldRender())
			{
				if(shapeComponent.isFill())
				{
				    shapeRenderer.rect(positionComponent.getX(), positionComponent.getY(), positionComponent.getWidth(), positionComponent.getHeight());
				}
			}
		}
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.GREEN);
		for (Entity entity : entities)
		{
			ShapeRenderableComponent shapeComponent = shapeComponents.get(entity);
			PositionComponent positionComponent = positionComponents.get(entity);
			if (shapeComponent.isShouldRender())
			{
				shapeRenderer.rect(positionComponent.getX(), positionComponent.getY(), positionComponent.getWidth(), positionComponent.getHeight());
			}
		}
	    shapeRenderer.end();
		spriteBatch.begin();
	}
	
	public void handleSetShapeRender(SetShapeRenderEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			ShapeRenderableComponent shapeComponent = shapeComponents.get(entity);
			EntityComponent entityComponent = entityComponents.get(entity);
			
			if (entityComponent != null && event.getEntityID().equals(entityComponent.getId()))
			{
				shapeComponent.setShouldRender(event.isShouldRender());
			}
		}
	}

}
