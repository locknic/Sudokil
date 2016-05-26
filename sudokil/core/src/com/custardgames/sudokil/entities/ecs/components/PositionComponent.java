package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent extends Component
{
	private float x, y;
	private float width, height;
	private float angle;
	private Vector2 tempVector;

	public PositionComponent()
	{
		tempVector = new Vector2();
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

	public float orientateDirectionX(float xDir, float yDir)
	{
		tempVector.x = xDir;
		tempVector.y = yDir;
		return tempVector.rotate(angle).x;
	}

	public float orientateDirectionY(float xDir, float yDir)
	{
		tempVector.x = xDir;
		tempVector.y = yDir;
		return tempVector.rotate(angle).y;
	}

}
