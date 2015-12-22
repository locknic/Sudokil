package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class CharacterInputComponent extends Component
{
	private boolean left, right, up, down, space, shift, control, action1, action2;
	private boolean released;

	public CharacterInputComponent()
	{
		setAllFalse();
	}

	public void setAllFalse()
	{
		left = right = up = down = space = shift = control = action1 = action2 = false;
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

	public boolean isSpace()
	{
		return space;
	}

	public void setSpace(boolean space)
	{
		this.space = space;
	}

	public boolean isShift()
	{
		return shift;
	}

	public void setShift(boolean shift)
	{
		this.shift = shift;
	}

	public boolean isControl()
	{
		return control;
	}

	public void setControl(boolean control)
	{
		this.control = control;
	}

	public boolean isAction1()
	{
		return action1;
	}

	public void setAction1(boolean action1)
	{
		this.action1 = action1;
	}

	public boolean isAction2()
	{
		return action2;
	}

	public void setAction2(boolean action2)
	{
		this.action2 = action2;
	}

	public boolean isReleased()
	{
		return released;
	}

	public void setReleased(boolean released)
	{
		this.released = released;
	}

}
