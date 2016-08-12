package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class ShapeRenderableComponent extends Component
{
	private boolean fill;
	private boolean shouldRender;

	public boolean isFill()
	{
		return fill;
	}

	public void setShouldRender(boolean shouldRender)
	{
		this.shouldRender = shouldRender;
	}

	public boolean isShouldRender()
	{
		return shouldRender;
	}
}
