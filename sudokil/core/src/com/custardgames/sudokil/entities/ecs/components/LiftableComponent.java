package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class LiftableComponent extends Component
{
	private boolean lifted;

	public LiftableComponent()
	{
		lifted = false;
	}

	public boolean isLifted()
	{
		return lifted;
	}

	public void setLifted(boolean lifted)
	{
		this.lifted = lifted;
	}

}
