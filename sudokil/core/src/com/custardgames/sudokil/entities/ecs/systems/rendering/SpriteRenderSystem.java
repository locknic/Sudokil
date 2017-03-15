package com.custardgames.sudokil.entities.ecs.systems.rendering;

import java.util.Comparator;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.managers.CustardAssetManager;

public class SpriteRenderSystem extends EntityProcessingSystem
{
	private ComponentMapper<SpriteComponent> spriteComponents;
	private ComponentMapper<PositionComponent> positionComponents;

	private boolean released = false;
	private boolean oldSprites = false;

	@Wire
	private CustardAssetManager assetManager;


	public SpriteRenderSystem()
	{
		super(Aspect.all(SpriteComponent.class, PositionComponent.class));
	}

	@Override
	public boolean checkProcessing()
	{
		return true;
	}

	@Override
	protected void process(Entity arg0)
	{
		if (Gdx.input.isKeyPressed(Keys.F12))
		{
			if (released)
			{
				oldSprites = !oldSprites;
			}
			released = false;
		}
		else
		{
			released = true;
		}
	}

	public final Comparator<Entity> SpritezComparator = new Comparator<Entity>()
	{
		@Override
		public int compare(Entity o1, Entity o2)
		{
			SpriteComponent spriteComponent1 = spriteComponents.get(o1);
			SpriteComponent spriteComponent2 = spriteComponents.get(o2);
			return spriteComponent1.compareTo(spriteComponent2);
		}
	};

	public final Comparator<Entity> PositionYComparator = new Comparator<Entity>()
	{
		@Override
		public int compare(Entity o1, Entity o2)
		{
			PositionComponent positionComponent1 = positionComponents.get(o1);
			PositionComponent positionComponent2 = positionComponents.get(o2);
			return ((Float)positionComponent2.getExpectedY()).compareTo(positionComponent1.getExpectedY());
		}
	};

	public void render(Batch spriteBatch)
	{
		Sprite sprite;
		Bag<Entity> entities = getEntities();
		entities.sort(PositionYComparator);
		for (Entity entity : entities)
		{
			SpriteComponent spriteComponent = spriteComponents.get(entity);
			PositionComponent positionComponent = positionComponents.get(entity);
			if (spriteComponent.isShouldRender() && spriteComponent.getSpriteLocation() != null)
			{
				if (spriteComponent.getWidth() == 0 || spriteComponent.getHeight() == 0)
				{
					sprite = new Sprite((Texture) assetManager.get(spriteComponent.getSpriteLocation(), Texture.class), (int) positionComponent.getWidth(),
							(int) positionComponent.getHeight());
					sprite.setPosition(positionComponent.getX() + spriteComponent.getxOffset(), positionComponent.getY() + spriteComponent.getyOffset());
				}
				else
				{
					sprite = new Sprite((Texture) assetManager.get(spriteComponent.getSpriteLocation(), Texture.class), (int) spriteComponent.getWidth(),
							(int) spriteComponent.getHeight());
					sprite.setPosition(positionComponent.getX() + spriteComponent.getxOffset(), positionComponent.getY() + spriteComponent.getyOffset());
				}
				if (oldSprites)
					sprite.rotate(positionComponent.getAngle());
				sprite.draw(spriteBatch);
			}
		}
	}

}
