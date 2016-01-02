package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class PositionComponent extends Component
{
	private float x, y;
	private float width, height;
	private float angle;

	public PositionComponent()
	{
		
	}
	
	public PositionComponent(int x, int y)
	{
		setX(x);
		setY(y);
	}

	public float getX()
	{
		return x;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public float getY()
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
	}

	public void setPosition(float x, float y)
	{
		setX(x);
		setY(y);
	}

	public float getWidth()
	{
		return width;
	}

	public void setWidth(float width)
	{
		this.width = width;
	}

	public float getHeight()
	{
		return height;
	}

	public void setHeight(float height)
	{
		this.height = height;
	}

	public float getAngle()
	{
		return angle;
	}

	public void setAngle(float angle)
	{
		this.angle = angle;
	}

}
