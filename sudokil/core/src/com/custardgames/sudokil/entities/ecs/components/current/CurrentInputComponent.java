package com.custardgames.sudokil.entities.ecs.components.current;

import com.artemis.Component;

public class CurrentInputComponent extends Component
{
	private boolean activated;
	private boolean up, down, left, right, centre;

	public boolean isActivated()
	{
		return activated;
	}

	public void setActivated(boolean activated)
	{
		this.activated = activated;
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

	public boolean isCentre()
	{
		return centre;
	}

	public void setCentre(boolean centre)
	{
		this.centre = centre;
	}

}
