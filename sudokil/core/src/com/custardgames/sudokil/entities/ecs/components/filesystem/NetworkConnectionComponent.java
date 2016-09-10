package com.custardgames.sudokil.entities.ecs.components.filesystem;

import com.artemis.Component;

public class NetworkConnectionComponent extends Component
{
	private boolean left, right, up, down;

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

}
