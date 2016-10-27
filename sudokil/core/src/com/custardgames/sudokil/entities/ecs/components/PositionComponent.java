package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent extends Component
{
	private float x, y;
	private float expectedX, expectedY;
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

	public float getExpectedX()
	{
		return expectedX;
	}

	public void setExpectedX(float expectedX)
	{
		this.expectedX = expectedX;
	}

	public float getY()
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
	}

	public float getExpectedY()
	{
		return expectedY;
	}

	public void setExpectedY(float expectedY)
	{
		this.expectedY = expectedY;
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
	
	public static float getNormalAngle(float angle)
	{
		while (angle >= 360)
		{
			angle -= 360;
		}
		
		while (angle < 0)
		{
			angle += 360;
		}
		
		return angle;
	}
	
	public float getNormalAngle()
	{
		return getNormalAngle(angle);
	}

	public int orientateDirectionX(float xDir, float yDir)
	{
		tempVector.x = xDir;
		tempVector.y = yDir;
		tempVector.rotate(angle);
		return (int) tempVector.x;
	}

	public int orientateDirectionY(float xDir, float yDir)
	{
		tempVector.x = xDir;
		tempVector.y = yDir;
		tempVector.rotate(angle);
		return (int) tempVector.y;
	}
	
	public int unOrientateDirectionX(float xDir, float yDir)
	{
		tempVector.x = xDir;
		tempVector.y = yDir;
		tempVector.rotate(-angle);
		return (int) tempVector.x;
	}

	public int unOrientateDirectionY(float xDir, float yDir)
	{
		tempVector.x = xDir;
		tempVector.y = yDir;
		tempVector.rotate(-angle);
		return (int) tempVector.y;
	}

}
