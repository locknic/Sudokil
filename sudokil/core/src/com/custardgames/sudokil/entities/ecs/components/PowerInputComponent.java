package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class PowerInputComponent extends Component
{
	private boolean activated;
	private boolean left, right, up, down, centre;
	
	public PowerInputComponent()
	{
		this(true, false, false, false, false, false);
	}
	
	public PowerInputComponent(boolean activated, boolean left, boolean right, boolean up, boolean down, boolean centre)
	{
		this.activated = activated;
		this.left = left;
		this.right = right;
		this.up = up;
		this.down = down;
		this.centre = centre;
	}

	public boolean isActivated()
	{
		return activated;
	}

	public void setActivated(boolean activated)
	{
		this.activated = activated;
	}

	public boolean isLeft()
	{
		return left;
	}

	public boolean isRight()
	{
		return right;
	}

	public boolean isUp()
	{
		return up;
	}

	public boolean isDown()
	{
		return down;
	}

	public boolean isCentre()
	{
		return centre;
	}
}
