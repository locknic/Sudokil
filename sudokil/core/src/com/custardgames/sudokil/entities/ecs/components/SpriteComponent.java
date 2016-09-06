package com.custardgames.sudokil.entities.ecs.components;

import java.util.Comparator;

import com.artemis.Component;

public class SpriteComponent extends Component implements Comparator<SpriteComponent>, Comparable<SpriteComponent>
{
	private String spriteLocation;
	private float width, height;
	private boolean shouldRender;
	private int zOrder;

	public SpriteComponent()
	{
		setShouldRender(true);
		zOrder = 50;
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

	public int getzOrder()
	{
		return zOrder;
	}

	public void setzOrder(int zOrder)
	{
		this.zOrder = zOrder;
	}

	@Override
	public int compareTo(SpriteComponent o)
	{
		return ((Integer) zOrder).compareTo(o.getzOrder());
	}

	@Override
	public int compare(SpriteComponent o1, SpriteComponent o2)
	{
		return o1.compareTo(o2);
	}

	public float getHeight()
	{
		return height;
	}

	public void setHeight(float height)
	{
		this.height = height;
	}

	public float getWidth()
	{
		return width;
	}

	public void setWidth(float width)
	{
		this.width = width;
	}

}
