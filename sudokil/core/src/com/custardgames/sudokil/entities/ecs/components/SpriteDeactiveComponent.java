package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class SpriteDeactiveComponent extends Component
{
	private String onSpriteLocation;
	private String offSpriteLocation;
	private boolean shouldRender;

	public SpriteDeactiveComponent()
	{
		setShouldRender(true);
	}

	public SpriteDeactiveComponent(String spriteLocation)
	{
		this.setOnSpriteLocation(spriteLocation);
	}

	public String getOnSpriteLocation()
	{
		return onSpriteLocation;
	}

	public void setOnSpriteLocation(String onSpriteLocation)
	{
		this.onSpriteLocation = onSpriteLocation;
	}

	public String getOffSpriteLocation()
	{
		return offSpriteLocation;
	}

	public void setOffSpriteLocation(String offSpriteLocation)
	{
		this.offSpriteLocation = offSpriteLocation;
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
