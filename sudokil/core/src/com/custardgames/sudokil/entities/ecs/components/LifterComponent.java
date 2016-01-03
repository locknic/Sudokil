package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;
import com.artemis.Entity;

public class LifterComponent extends Component
{
	private boolean lifting;
	private Entity lifted;
	private float liftSpeed;

	public LifterComponent()
	{
		lifting = false;
		lifted = null;
		setLiftSpeed(0.5f);
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

	public float getLiftSpeed()
	{
		return liftSpeed;
	}

	public void setLiftSpeed(float liftSpeed)
	{
		this.liftSpeed = liftSpeed;
	}
	
}
