package com.custardgames.sudokil.entities.ecs.systems.rendering;

import java.util.Comparator;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.PowerInputComponent;
import com.custardgames.sudokil.entities.ecs.components.PowerOutputComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.entities.ecs.components.entities.WireSpritesComponent;

public class WireRenderSystem extends EntityProcessingSystem
{
	private ComponentMapper<SpriteComponent> spriteComponents;
	private ComponentMapper<PositionComponent> positionComponents;
	private ComponentMapper<PowerInputComponent> powerInputComponents;
	private ComponentMapper<PowerOutputComponent> powerOutputComponents;

	@Wire
	private AssetManager assetManager;

	@SuppressWarnings("unchecked")
	public WireRenderSystem()
	{
		super(Aspect.all(SpriteComponent.class, PositionComponent.class, WireSpritesComponent.class).one(PowerInputComponent.class, PowerOutputComponent.class));
	}

	@Override
	public boolean checkProcessing()
	{
		return false;
	}

	@Override
	protected void process(Entity e)
	{
		// TODO Auto-generated method stub
		
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

	public void render(Batch spriteBatch)
	{
		Sprite sprite;
		Bag<Entity> entities = getEntities();
		entities.sort(SpritezComparator);
		for (Entity entity : entities)
		{
			SpriteComponent spriteComponent = spriteComponents.get(entity);
			PositionComponent positionComponent = positionComponents.get(entity);
			PowerInputComponent powerInputComponent = powerInputComponents.get(entity);
			PowerOutputComponent powerOutputComponent = powerOutputComponents.get(entity);
			
			boolean isLeft = (powerInputComponent != null && powerInputComponent.isLeft()) || (powerOutputComponent != null && powerOutputComponent.isLeft());
			boolean isRight = (powerInputComponent != null && powerInputComponent.isRight()) || (powerOutputComponent != null && powerOutputComponent.isRight());
			boolean isUp = (powerInputComponent != null && powerInputComponent.isUp()) || (powerOutputComponent != null && powerOutputComponent.isUp());
			boolean isDown = (powerInputComponent != null && powerInputComponent.isDown()) || (powerOutputComponent != null && powerOutputComponent.isDown());

			if (spriteComponent.isShouldRender())
			{
				if (isLeft)
				{
					sprite = new Sprite((Texture) assetManager.get("images/entities/wire_left.png"), (int) positionComponent.getWidth(),
							(int) positionComponent.getHeight());
					sprite.setPosition(positionComponent.getX(), positionComponent.getY());
					sprite.draw(spriteBatch);
				}
				if (isRight)
				{
					sprite = new Sprite((Texture) assetManager.get("images/entities/wire_right.png"), (int) positionComponent.getWidth(),
							(int) positionComponent.getHeight());
					sprite.setPosition(positionComponent.getX(), positionComponent.getY());
					sprite.draw(spriteBatch);
				}
				if (isUp)
				{
					sprite = new Sprite((Texture) assetManager.get("images/entities/wire_up.png"), (int) positionComponent.getWidth(),
							(int) positionComponent.getHeight());
					sprite.setPosition(positionComponent.getX(), positionComponent.getY());
					sprite.draw(spriteBatch);
				}
				if (isDown)
				{
					sprite = new Sprite((Texture) assetManager.get("images/entities/wire_down.png"), (int) positionComponent.getWidth(),
							(int) positionComponent.getHeight());
					sprite.setPosition(positionComponent.getX(), positionComponent.getY());
					sprite.draw(spriteBatch);
				}
				
			}
		}
	}

}
