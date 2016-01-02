package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;
import com.artemis.Entity;

public class LifterComponent extends Component
{
	private boolean lifting;
	private Entity lifted;

	public LifterComponent()
	{
		lifting = false;
		lifted = null;
	}
	
	public boolean isLifting()
	{
		return lifting;
	}

	public void setLifting(boolean lifting)
	{
		this.lifting = lifting;
	}

	public Entity getLifted()
	{
		return lifted;
	}

	public void setLifted(Entity lifted)
	{
		this.lifted = lifted;
	}
	
}
