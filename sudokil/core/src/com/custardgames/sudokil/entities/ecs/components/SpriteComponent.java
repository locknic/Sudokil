package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class SpriteComponent extends Component
{
	private String spriteLocation;
	private boolean shouldRender;

	public SpriteComponent()
	{
		setShouldRender(true);
	}

	public SpriteComponent(String spriteLocation)
	{
		this.setSpriteLocation(spriteLocation);
	}

	public String getSpriteLocation()
	{
		return spriteLocation;
	}

	public void setSpriteLocation(String spriteLocation)
	{
		this.spriteLocation = spriteLocation;
	}

	public boolean isShouldRender()
	{
		return shouldRender;
	}

	public void setShouldRender(boolean shouldRender)
	{
		this.shouldRender = shouldRender;
	}

}
