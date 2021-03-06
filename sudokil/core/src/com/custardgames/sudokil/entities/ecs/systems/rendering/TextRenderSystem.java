package com.custardgames.sudokil.entities.ecs.systems.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.TextTagComponent;
import com.custardgames.sudokil.managers.CustardAssetManager;

public class TextRenderSystem extends EntityProcessingSystem
{
	private ComponentMapper<TextTagComponent> textTagComponents;
	private ComponentMapper<PositionComponent> positionComponents;
	private ComponentMapper<EntityComponent> entityComponents;

	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;
	private BitmapFont font;

	@Wire
	private CustardAssetManager assetManager;


	public TextRenderSystem()
	{
		super(Aspect.all(TextTagComponent.class, PositionComponent.class));

		generator = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/TerminusTTF-Bold-4.39.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 16;
		parameter.minFilter = Texture.TextureFilter.Nearest;
		parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		font = generator.generateFont(parameter);
		generator.dispose();

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
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			TextTagComponent textTagComponent = textTagComponents.get(entity);
			PositionComponent positionComponent = positionComponents.get(entity);
			EntityComponent entityComponent = entityComponents.get(entity);
			
			if (textTagComponent.isShouldRender())
			{
				String text = textTagComponent.getText();
				if (text == null || text.equals(""))
				{
					if (entityComponent != null)
					{
						text = entityComponent.getId();
					}
				}
				GlyphLayout layout = new GlyphLayout(font, text);
				font.setColor(Color.BLACK);
				font.draw(spriteBatch, text,
						positionComponent.getX() + positionComponent.getWidth() / 2 + textTagComponent.getDeltaX() - layout.width / 2 - 1,
						positionComponent.getY() + positionComponent.getHeight() + textTagComponent.getDeltaY() + layout.height);
				font.draw(spriteBatch, text,
						positionComponent.getX() + positionComponent.getWidth() / 2 + textTagComponent.getDeltaX() - layout.width / 2,
						positionComponent.getY() + positionComponent.getHeight() + textTagComponent.getDeltaY() + layout.height - 1);
				font.draw(spriteBatch, text,
						positionComponent.getX() + positionComponent.getWidth() / 2 + textTagComponent.getDeltaX() - layout.width / 2 + 1,
						positionComponent.getY() + positionComponent.getHeight() + textTagComponent.getDeltaY() + layout.height);
				font.draw(spriteBatch, text,
						positionComponent.getX() + positionComponent.getWidth() / 2 + textTagComponent.getDeltaX() - layout.width / 2,
						positionComponent.getY() + positionComponent.getHeight() + textTagComponent.getDeltaY() + layout.height + 1);
				font.setColor(Color.WHITE);
				font.draw(spriteBatch, text,
						positionComponent.getX() + positionComponent.getWidth() / 2 + textTagComponent.getDeltaX() - layout.width / 2,
						positionComponent.getY() + positionComponent.getHeight() + textTagComponent.getDeltaY() + layout.height);
			}
		}
	}

}
