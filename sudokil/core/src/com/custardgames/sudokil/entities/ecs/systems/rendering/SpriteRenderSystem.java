package com.custardgames.sudokil.entities.ecs.systems.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;

public class SpriteRenderSystem extends EntityProcessingSystem
{
	private ComponentMapper<SpriteComponent> spriteComponents;
	private ComponentMapper<PositionComponent> positionComponents;

	@Wire
	private AssetManager assetManager;

	@SuppressWarnings("unchecked")
	public SpriteRenderSystem()
	{
		super(Aspect.all(SpriteComponent.class, PositionComponent.class));
	}

	@Override
	public boolean checkProcessing()
	{
		return false;
	}

	@Override
	protected void process(Entity arg0)
	{

	}

	public void render(Batch spriteBatch)
	{
		Sprite sprite;
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			SpriteComponent spriteComponent = spriteComponents.get(entity);
			PositionComponent positionComponent = positionComponents.get(entity);
			if (spriteComponent.isShouldRender())
			{
				sprite = new Sprite((Texture) assetManager.get(spriteComponent.getSpriteLocation()), (int) positionComponent.getWidth(),
						(int) positionComponent.getHeight());
				sprite.setPosition(positionComponent.getX(), positionComponent.getY());
				sprite.rotate(positionComponent.getAngle());
				sprite.draw(spriteBatch);
			}
		}
	}

}
