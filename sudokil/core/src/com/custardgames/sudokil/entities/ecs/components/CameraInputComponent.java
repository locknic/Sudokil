package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class CameraInputComponent extends Component
{
	private boolean up, down, left, right, zoomIn, zoomOut, reset;
	private float mouseX, mouseY;
	private boolean mousePressing;

	public CameraInputComponent()
	{
		reset = true;
	}
	
	public boolean isUp()
	{
		return up;
	}

	public void setUp(boolean up)
	{
		this.up = up;
	}

	public boolean isDown()
	{
		return down;
	}

	public void setDown(boolean down)
	{
		this.down = down;
	}

	public boolean isLeft()
	{
		return left;
	}

	public void setLeft(boolean left)
	{
		this.left = left;
	}

	public boolean isRight()
	{
		return right;
	}

	public void setRight(boolean right)
	{
		this.right = right;
	}

	public boolean isZoomIn()
	{
		return zoomIn;
	}

	public void setZoomIn(boolean zoomIn)
	{
		this.zoomIn = zoomIn;
	}

	public boolean isZoomOut()
	{
		return zoomOut;
	}

	public void setZoomOut(boolean zoomOut)
	{
		this.zoomOut = zoomOut;
	}

	public boolean isReset()
	{
		return reset;
	}

	public void setReset(boolean reset)
	{
		this.reset = reset;
	}

	public float getMouseY()
	{
		return mouseY;
	}

	public void setMouseY(float mouseY)
	{
		this.mouseY = mouseY;
	}

	public float getMouseX()
	{
		return mouseX;
	}

	public void setMouseX(float mouseX)
	{
		this.mouseX = mouseX;
	}

	public boolean isMousePressing()
	{
		return mousePressing;
	}

	public void setMousePressing(boolean mousePressing)
	{
		this.mousePressing = mousePressing;
	}
}
