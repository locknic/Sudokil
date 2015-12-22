package com.custardgames.sudokil.entities.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;

public class SpriteRenderSystem extends EntityProcessingSystem
{
	private ComponentMapper<SpriteComponent> spriteComponents;
	private ComponentMapper<PositionComponent> positionComponents;

	private AssetManager assetManager;

	@SuppressWarnings("unchecked")
	public SpriteRenderSystem(AssetManager assetManager)
	{
		super(Aspect.all(SpriteComponent.class));

		this.assetManager = assetManager;
	}

	@Override
	protected void process(Entity arg0)
	{
		// TODO Auto-generated method stub

	}

	public void render(SpriteBatch spriteBatch)
	{
		spriteBatch.begin();
		Sprite sprite;
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			SpriteComponent spriteComponent = spriteComponents.get(entities.get(x));
			PositionComponent positionComponent = positionComponents.get(entities.get(x));
			if (spriteComponent.isShouldRender())
			{
				sprite = new Sprite((Texture) assetManager.get(spriteComponent.getSpriteLocation()),
						(int) positionComponent.getWidth(), (int) positionComponent.getHeight());
				sprite.setPosition(positionComponent.getX(), positionComponent.getY());
				sprite.rotate(positionComponent.getAngle());
				sprite.draw(spriteBatch);
			}
		}
		spriteBatch.end();
	}

}
